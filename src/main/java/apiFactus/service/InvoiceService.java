package apiFactus.service;

import apiFactus.dto.*;
import apiFactus.model.*;
import apiFactus.model.Customer;
import apiFactus.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthService authService;
    private final InvoiceRepository invoiceRepository;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final MunicipalityRepository MunicipalityRepository;
    private final TributeRepository tributeRepository;
    private final LegalOrganizationRepository legalOrganizationRepository;

    @Value("${factus.api.url}")
    private String apiUrl;

    public InvoiceService(
            @Qualifier("apiRestTemplate") RestTemplate restTemplate,
            AuthService authService,
            InvoiceRepository invoiceRepository,
            CustomerService customerService,
            CustomerRepository customerRepository,
            MunicipalityRepository municipalityRepository,
            TributeRepository tributeRepository,
            LegalOrganizationRepository legalOrganizationRepository) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.invoiceRepository = invoiceRepository;
        this.customerService = customerService ;
        this.customerRepository = customerRepository;
        this.MunicipalityRepository = municipalityRepository;
        this.tributeRepository = tributeRepository;
        this.legalOrganizationRepository = legalOrganizationRepository;
    }

    @Transactional
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequest) {
        logger.debug("Recibida solicitud para crear factura: {}", invoiceRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + authService.getAccessToken());

        HttpEntity<InvoiceRequestDTO> request = new HttpEntity<>(invoiceRequest, headers);

        try {
            logger.debug("Enviando solicitud para crear factura a: {}", apiUrl + "/v1/bills/validate");
            ResponseEntity<InvoiceResponseDTO> response = restTemplate.postForEntity(
                    apiUrl + "/v1/bills/validate",
                    request,
                    InvoiceResponseDTO.class);

            InvoiceResponseDTO responseBody = response.getBody();
            logger.debug("Respuesta completa de la API: {}", responseBody);

            if (responseBody != null && "Created".equals(responseBody.getStatus())) {
                logger.debug("Factura creada: status_code=201, invoice_id={}",
                        responseBody.getData() != null && responseBody.getData().getBill() != null
                                ? responseBody.getData().getBill().getId() : "N/A");
                saveInvoiceToDatabase(invoiceRequest, responseBody);
                return responseBody;
            } else {
                logger.warn("Respuesta inválida de la API: status_code={}, message={}",
                        response.getStatusCodeValue(), responseBody != null ? responseBody.getMessage() : "Respuesta nula");
                return responseBody;
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.warn("Error de autorización: status_code=401, intentando refrescar token");
            authService.refreshToken();
            headers.set("Authorization", "Bearer " + authService.getAccessToken());
            request = new HttpEntity<>(invoiceRequest, headers);

            logger.debug("Reintentando solicitud para crear factura a: {}", apiUrl + "/v1/bills/validate");
            ResponseEntity<InvoiceResponseDTO> retryResponse = restTemplate.postForEntity(
                    apiUrl + "/v1/bills/validate",
                    request,
                    InvoiceResponseDTO.class);

            InvoiceResponseDTO retryResponseBody = retryResponse.getBody();
            logger.debug("Respuesta de reintento: status_code={}, body={}",
                    retryResponse.getStatusCodeValue(), retryResponseBody);

            if (retryResponseBody != null && "Created".equals(retryResponseBody.getStatus())) {
                saveInvoiceToDatabase(invoiceRequest, retryResponseBody);
            }
            return retryResponseBody;
        } catch (HttpClientErrorException e) {
            logger.error("Error en la API: status_code={}, message={}",
                    e.getStatusCode().value(), e.getResponseBodyAsString());
            InvoiceResponseDTO errorResponse = new InvoiceResponseDTO();
            errorResponse.setStatus("Error");
            errorResponse.setMessage("Error en la API: " + e.getResponseBodyAsString());
            return errorResponse;
        } catch (Exception e) {
            logger.error("Error al crear la factura: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear la factura: " + e.getMessage(), e);
        }
    }


    @Transactional
    public void saveInvoiceToDatabase(InvoiceRequestDTO invoiceRequest, InvoiceResponseDTO invoiceResponse) {
        logger.debug("Iniciando guardado de factura en la base de datos");

        try {
            Objects.requireNonNull(invoiceRequest, "InvoiceRequestDTO cannot be null");
            Objects.requireNonNull(invoiceResponse, "InvoiceResponseDTO cannot be null");
            Objects.requireNonNull(invoiceResponse.getData(), "InvoiceResponseDTO data cannot be null");
            Objects.requireNonNull(invoiceResponse.getData().getBill(), "InvoiceResponseDTO bill data cannot be null");

            // Validar invoiceNumber
            String invoiceNumber = invoiceResponse.getData().getBill().getNumber();
            if (invoiceNumber == null || invoiceNumber.isEmpty()) {
                logger.error("El número de factura es nulo o vacío: {}", invoiceResponse);
                throw new IllegalArgumentException("El número de factura no puede estar vacío");
            }
            logger.debug("Número de factura recibido: {}", invoiceNumber);

            // Verificar si la factura ya existe
            Invoice existingInvoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
            if (existingInvoice != null) {
                logger.warn("La factura con número {} ya existe en la base de datos", invoiceNumber);
                return; // O actualiza la factura existente según tu lógica
            }

            // Buscar o crear el cliente
            logger.debug("Buscando cliente con identificación: {}", invoiceRequest.getCustomer().getIdentification());
            Customer customer = customerRepository.findByIdentification(invoiceRequest.getCustomer().getIdentification());
            if (customer == null) {
                logger.debug("Creando nuevo cliente");
                customer = new Customer();
                customer.setIdentificationDocumentId(invoiceRequest.getCustomer().getIdentification_document_id());
                customer.setIdentification(invoiceRequest.getCustomer().getIdentification());
                customer.setDv(invoiceRequest.getCustomer().getDv());
                customer.setGraphic_representation_name(invoiceRequest.getCustomer().getGraphic_representation_name());
                customer.setCompany(invoiceRequest.getCustomer().getCompany());
                customer.setTradeName(invoiceRequest.getCustomer().getTrade_name());
                customer.setNames(invoiceRequest.getCustomer().getNames());
                customer.setAddress(invoiceRequest.getCustomer().getAddress());
                customer.setEmail(invoiceRequest.getCustomer().getEmail());
                customer.setPhone(invoiceRequest.getCustomer().getPhone());

                // Configurar relaciones (LegalOrganization, Tribute, Municipality)
                if (invoiceRequest.getCustomer().getLegalOrganizationId() != null) {
                    Integer legalOrgId = Integer.valueOf(invoiceRequest.getCustomer().getLegalOrganizationId());
                    LegalOrganization legalOrganization = legalOrganizationRepository.findById(legalOrgId)
                            .orElseGet(() -> {
                                logger.debug("Creando nueva organización legal con ID: {}", legalOrgId);
                                LegalOrganization newLegalOrganization = new LegalOrganization();
                                newLegalOrganization.setId(legalOrgId);
                                newLegalOrganization.setCode("DEFAULT_CODE");
                                newLegalOrganization.setName("DEFAULT_NAME");
                                return legalOrganizationRepository.save(newLegalOrganization);
                            });
                    customer.setLegal_organization(legalOrganization);
                }

                if (invoiceRequest.getCustomer().getTributeId() != null) {
                    Integer tributeId = Integer.valueOf(invoiceRequest.getCustomer().getTributeId());
                    Tribute tribute = tributeRepository.findById(tributeId)
                            .orElseGet(() -> {
                                logger.debug("Creando nuevo tributo con ID: {}", tributeId);
                                Tribute newTribute = new Tribute();
                                newTribute.setId(tributeId);
                                newTribute.setCode("DEFAULT_CODE");
                                newTribute.setName("DEFAULT_NAME");
                                return tributeRepository.save(newTribute);
                            });
                    customer.setTribute(tribute);
                }

                if (invoiceRequest.getCustomer().getMunicipalityId() != null) {
                    Integer municipalityId = Integer.valueOf(invoiceRequest.getCustomer().getMunicipalityId());
                    Municipality municipality = MunicipalityRepository.findById(municipalityId)
                            .orElseGet(() -> {
                                logger.debug("Creando nuevo municipio con ID: {}", municipalityId);
                                Municipality newMunicipality = new Municipality();
                                newMunicipality.setId(municipalityId);
                                newMunicipality.setCode("DEFAULT_CODE");
                                newMunicipality.setName("DEFAULT_NAME");
                                return MunicipalityRepository.save(newMunicipality);
                            });
                    customer.setMunicipality(municipality);
                }

                customer = customerRepository.save(customer);
                logger.debug("Cliente guardado con ID: {}", customer.getId());
            } else {
                logger.debug("Cliente encontrado con ID: {}", customer.getId());
            }

            // Crear la factura
            logger.debug("Creando factura con número: {}", invoiceNumber);
            Invoice invoice = new Invoice();
            invoice.setFactusInvoiceId(invoiceResponse.getData().getBill().getId());
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setInvoiceUuid(invoiceResponse.getData().getBill().getCufe());
            invoice.setStatus(invoiceResponse.getData().getBill().getStatus());
            invoice.setReferenceCode(invoiceRequest.getReference_code());
            invoice.setObservation(invoiceRequest.getObservation());
            invoice.setPaymentForm(invoiceRequest.getPayment_form());
            invoice.setPaymentDueDate(invoiceRequest.getPayment_due_date());
            invoice.setPaymentMethodCode(invoiceRequest.getPayment_method_code());
            invoice.setCreatedAt(LocalDateTime.now());
            invoice.setUpdatedAt(LocalDateTime.now());
            invoice.setCustomer(customer);

            // Guardar los ítems de la factura
            List<InvoiceItem> items = new ArrayList<>();
            for (ItemDTO itemDTO : invoiceRequest.getItems()) {
                logger.debug("Procesando ítem: {}", itemDTO.getName());
                InvoiceItem item = new InvoiceItem();
                item.setInvoice(invoice);
                item.setCodeReference(itemDTO.getCode_reference());
                item.setName(itemDTO.getName());
                item.setQuantity(itemDTO.getQuantity());
                item.setDiscountRate(itemDTO.getDiscount_rate());
                item.setDiscount(itemDTO.getDiscount());
                item.setGrossValue(itemDTO.getGrossValue());
                item.setTaxRate(itemDTO.getTax_rate());
                item.setTaxableAmount(itemDTO.getTaxableAmount());
                item.setTaxAmount(itemDTO.getTaxAmount());
                item.setPrice(itemDTO.getPrice());
                item.setTotal(itemDTO.getTotal());
                item.setIsExcluded(itemDTO.getIs_excluded());

                // Configurar relaciones (UnitMeasure, StandardCode, Tribute)
                if (itemDTO.getUnit_measure() != null) {
                    logger.debug("Configurando unidad de medida para ítem: {}", itemDTO.getName());
                    UnitMeasure unitMeasure = new UnitMeasure();
                    unitMeasure.setId(itemDTO.getUnit_measure().getId());
                    unitMeasure.setCode(itemDTO.getUnit_measure().getCode());
                    unitMeasure.setName(itemDTO.getUnit_measure().getName());
                    item.setUnitMeasure(unitMeasure);
                }

                if (itemDTO.getStandard_code() != null) {
                    logger.debug("Configurando código estándar para ítem: {}", itemDTO.getName());
                    StandardCode standardCode = new StandardCode();
                    standardCode.setId(itemDTO.getStandard_code().getId());
                    standardCode.setCode(itemDTO.getStandard_code().getCode());
                    standardCode.setName(itemDTO.getStandard_code().getName());
                    item.setStandardCode(standardCode);
                }

                if (itemDTO.getTribute() != null) {
                    logger.debug("Configurando tributo para ítem: {}", itemDTO.getName());
                    Tribute tribute = new Tribute();
                    tribute.setId(itemDTO.getTribute().getId());
                    tribute.setCode(itemDTO.getTribute().getCode());
                    tribute.setName(itemDTO.getTribute().getName());
                    item.setTribute(tribute);
                }

                // Guardar las retenciones
                List<WithholdingTax> withholdingTaxes = new ArrayList<>();
                if (itemDTO.getWithholding_taxes() != null) {
                    for (WithholdingTaxDTO taxDTO : itemDTO.getWithholding_taxes()) {
                        logger.debug("Procesando retención para ítem: {}", itemDTO.getName());
                        WithholdingTax tax = new WithholdingTax();
                        tax.setInvoiceItem(item);
                        tax.setCode(taxDTO.getCode());
                        tax.setName(taxDTO.getName());
                        tax.setWithholdingTaxRate(taxDTO.getValue());

                        List<WithholdingTaxRate> rates = new ArrayList<>();
                        if (taxDTO.getRates() != null) {
                            for (RateDTO rateDTO : taxDTO.getRates()) {
                                WithholdingTaxRate rate = new WithholdingTaxRate();
                                rate.setWithholdingTax(tax);
                                rate.setCode(rateDTO.getCode());
                                rate.setName(rateDTO.getName());
                                rate.setRate(rateDTO.getRate());
                                rates.add(rate);
                            }
                        }
                        tax.setRates(rates);
                        withholdingTaxes.add(tax);
                    }
                }
                item.setWithholdingTaxes(withholdingTaxes);
                items.add(item);
            }
            invoice.setItems(items);

            // Guardar la factura
            invoiceRepository.save(invoice);
            logger.debug("Factura guardada exitosamente con número: {}", invoiceNumber);
        } catch (Exception e) {
            logger.error("Error al guardar la factura en la base de datos: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo guardar la factura: " + e.getMessage(), e);
        }
    }


    public InvoiceResponseDTO validateInvoice(Integer invoiceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        // El token será añadido automáticamente por el TokenInterceptor

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            logger.debug("Enviando solicitud para validar factura a: {}", apiUrl + "/v1/bills/validate/" + invoiceId);
            ResponseEntity<InvoiceResponseDTO> response = restTemplate.exchange(
                    apiUrl + "/v1/bills/validate/" + invoiceId,
                    HttpMethod.POST,
                    request,
                    InvoiceResponseDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            // Si hay error de autorización, refrescamos el token y lo intentamos de nuevo
            authService.refreshToken();
            request = new HttpEntity<>(headers);

            logger.debug("Reintentando solicitud para validar factura a: {}", apiUrl + "/v1/bills/validate/" + invoiceId);
            ResponseEntity<InvoiceResponseDTO> response = restTemplate.exchange(
                    apiUrl + "/v1/bills/validate/" + invoiceId,
                    HttpMethod.POST,
                    request,
                    InvoiceResponseDTO.class);

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error al validar la factura: {}", e.getMessage(), e);
            throw new RuntimeException("Error al validar la factura: " + e.getMessage(), e);
        }
    }

    public InvoiceResponseDTO getInvoiceByNumber(String number) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + authService.getAccessToken());
        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            logger.info("Fetching invoice: {}/v1/bills/show/{}", apiUrl, number);
            ResponseEntity<InvoiceResponseDTO> response = restTemplate.exchange(
                    apiUrl + "/v1/bills/show/" + number,
                    HttpMethod.GET,
                    request,
                    InvoiceResponseDTO.class);
            logger.info("Invoice response: status={}, body={}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error fetching invoice {}: status={}, response={}",
                    number, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error fetching invoice: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error fetching invoice {}: {}", number, e.getMessage(), e);
            throw new RuntimeException("Error fetching invoice: " + e.getMessage());
        }
    }

    public byte[] downloadInvoicePdf(String number) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authService.getAccessToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PDF));
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            logger.info("Calling external API: {}/v1/bills/download-pdf/{}", apiUrl, number);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    apiUrl + "/v1/bills/download-pdf/" + number,
                    HttpMethod.GET,
                    request,
                    byte[].class);
            byte[] responseBytes = response.getBody();
            logger.info("Response status: {}, Content-Length: {}",
                    response.getStatusCode(), responseBytes != null ? responseBytes.length : 0);

            if (responseBytes == null || responseBytes.length == 0) {
                logger.error("Empty response for invoice {}", number);
                throw new RuntimeException("Empty PDF content received");
            }

            // Check if response is JSON
            String header = new String(responseBytes, 0, Math.min(responseBytes.length, 4));
            if (header.startsWith("{\"st")) {
                logger.info("Received JSON response, parsing...");
                JsonNode jsonNode = objectMapper.readTree(responseBytes);
                String status = jsonNode.get("status").asText();
                logger.debug("API response JSON: {}", jsonNode.toString()); // Log full response for debugging

                if (!"OK".equals(status)) {
                    String message = jsonNode.has("message") ? jsonNode.get("message").asText() : "Unknown error";
                    logger.error("API error for invoice {}: {}", number, message);
                    throw new RuntimeException("API error: " + message);
                }

                // Extract base64-encoded PDF
                JsonNode dataNode = jsonNode.get("data");
                if (dataNode == null) {
                    logger.error("No 'data' field in response for invoice {}", number);
                    throw new RuntimeException("No data in response");
                }

                String base64Pdf = null;
                if (dataNode.has("pdf_base64")) {
                    base64Pdf = dataNode.get("pdf_base64").asText();
                } else if (dataNode.has("pdf_base_64_encoded")) {
                    base64Pdf = dataNode.get("pdf_base_64_encoded").asText();
                    logger.warn("Using 'pdf_base_64_encoded' field for invoice {} due to API response", number);
                } else {
                    logger.error("No 'pdf_base64' or 'pdf_base_64_encoded' field in response for invoice {}", number);
                    throw new RuntimeException("No PDF data in response");
                }

                if (base64Pdf == null || base64Pdf.isEmpty()) {
                    logger.error("PDF base64 string is null or empty for invoice {}", number);
                    throw new RuntimeException("Invalid PDF data in response");
                }

                byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);
                logger.info("Decoded PDF size: {} bytes", pdfBytes.length);

                // Validate PDF header
                if (pdfBytes.length > 4 && !new String(pdfBytes, 0, 4).equals("%PDF")) {
                    logger.error("Invalid PDF header after decoding for invoice {}", number);
                    throw new RuntimeException("Invalid decoded PDF content");
                }
                return pdfBytes;
            } else if (header.equals("%PDF")) {
                logger.info("Received direct PDF response for invoice {}", number);
                return responseBytes;
            } else {
                String contentPreview = new String(responseBytes, 0, Math.min(responseBytes.length, 100));
                logger.error("Invalid response for invoice {}. Content preview: {}", number, contentPreview);
                throw new RuntimeException("Invalid PDF content received");
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error from external API for invoice {}: status={}, response={}",
                    number, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("External API error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error downloading PDF for invoice {}: {}", number, e.getMessage(), e);
            throw new RuntimeException("Error downloading PDF: " + e.getMessage());
        }
    }


    public byte[] downloadInvoiceXml(String number) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authService.getAccessToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            logger.info("Calling external API: {}/v1/bills/download-xml/{}", apiUrl, number);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    apiUrl + "/v1/bills/download-xml/" + number,
                    HttpMethod.GET,
                    request,
                    byte[].class);
            byte[] responseBytes = response.getBody();
            logger.info("Response status: {}, Content-Length: {}",
                    response.getStatusCode(), responseBytes != null ? responseBytes.length : 0);

            if (responseBytes == null || responseBytes.length == 0) {
                logger.error("Empty response for invoice XML {}", number);
                throw new RuntimeException("Empty XML content received");
            }

            // Check if response is JSON
            String header = new String(responseBytes, 0, Math.min(responseBytes.length, 4));
            if (header.startsWith("{\"st")) {
                logger.info("Received JSON response, parsing...");
                JsonNode jsonNode = objectMapper.readTree(responseBytes);
                logger.debug("Raw API response: {}", jsonNode.toString()); // Log full response for debugging

                String status = jsonNode.get("status").asText();
                if (!"OK".equals(status)) {
                    String message = jsonNode.has("message") ? jsonNode.get("message").asText() : "Unknown error";
                    logger.error("API error for invoice XML {}: {}", number, message);
                    throw new RuntimeException("API error: " + message);
                }

                // Extract base64-encoded XML
                JsonNode dataNode = jsonNode.get("data");
                if (dataNode == null || !dataNode.has("xml_base_64_encoded")) {
                    logger.error("No 'xml_base_64_encoded' field in response for invoice {}", number);
                    throw new RuntimeException("No XML data in response");
                }

                String base64Xml = dataNode.get("xml_base_64_encoded").asText();
                if (base64Xml == null || base64Xml.isEmpty()) {
                    logger.error("XML base64 string is null or empty for invoice {}", number);
                    throw new RuntimeException("Invalid XML data in response");
                }

                byte[] xmlBytes = Base64.getDecoder().decode(base64Xml);
                logger.info("Decoded XML size: {} bytes", xmlBytes.length);

                // Validate XML header (optional, to ensure it's valid XML)
                String xmlHeader = new String(xmlBytes, 0, Math.min(xmlBytes.length, 5));
                if (!xmlHeader.startsWith("<?xml")) {
                    logger.error("Invalid XML header after decoding for invoice {}", number);
                    throw new RuntimeException("Invalid decoded XML content");
                }

                return xmlBytes;
            } else if (header.startsWith("<?xml")) {
                logger.info("Received direct XML response for invoice {}", number);
                return responseBytes;
            } else {
                String contentPreview = new String(responseBytes, 0, Math.min(responseBytes.length, 100));
                logger.error("Invalid response for invoice XML {}. Content preview: {}", number, contentPreview);
                throw new RuntimeException("Invalid XML content received");
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error from external API for invoice XML {}: status={}, response={}",
                    number, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("External API error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error downloading XML for invoice {}: {}", number, e.getMessage(), e);
            throw new RuntimeException("Error downloading XML: " + e.getMessage());
        }
    }

    public PaginatedResponse<InvoiceListDTO> getPaginatedInvoices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);

        List<InvoiceListDTO> dtoList = invoicePage.getContent().stream()
                .map(invoice -> new InvoiceListDTO(
                        invoice.getId(),
                        invoice.getInvoiceNumber(),
                        invoice.getInvoiceUuid(),
                        invoice.getCustomer() != null ? invoice.getCustomer().getCompany() : "Sin cliente",
                        invoice.getCreatedAt(),
                        String.valueOf(invoice.getStatus())
                ))
                .toList();

        return new PaginatedResponse<>(dtoList, invoicePage.getTotalElements());
    }


    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }
}