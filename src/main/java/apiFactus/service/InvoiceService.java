package apiFactus.service;

import apiFactus.dto.*;
import apiFactus.model.*;
import apiFactus.model.Customer;
import apiFactus.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final RestTemplate restTemplate;
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        HttpEntity<InvoiceRequestDTO> request = new HttpEntity<>(invoiceRequest, headers);

        try {
            logger.debug("Enviando solicitud para crear factura a: {}", apiUrl + "/v1/bills/validate");
            ResponseEntity<InvoiceResponseDTO> response = restTemplate.postForEntity(
                    apiUrl + "/v1/bills/validate",
                    request,
                    InvoiceResponseDTO.class);

            InvoiceResponseDTO responseBody = response.getBody();
            if (responseBody != null && "Created".equals(responseBody.getStatus())) {
                // Manejar billing_period según su tipo
                if (responseBody.getBillingPeriod() != null) {
                    logger.debug("billing_period es una lista: {}", responseBody.getBillingPeriod());
                } else if (responseBody.getBillingPeriod() != null) {
                    logger.debug("billing_period es un objeto: {}", responseBody.getBillingPeriod());
                }
                saveInvoiceToDatabase(invoiceRequest, responseBody);
            }

            return responseBody;
        } catch (Exception e) {
            logger.error("Error al crear la factura: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear la factura: " + e.getMessage(), e);
        }
    }


    @Transactional
    public void saveInvoiceToDatabase(InvoiceRequestDTO invoiceRequest, InvoiceResponseDTO invoiceResponse) {

        Objects.requireNonNull(invoiceRequest, "InvoiceRequestDTO cannot be null");
        Objects.requireNonNull(invoiceResponse, "InvoiceResponseDTO cannot be null");
        Objects.requireNonNull(invoiceResponse.getData(), "InvoiceResponseDTO data cannot be null");
        Objects.requireNonNull(invoiceResponse.getData().getBill(), "InvoiceResponseDTO bill data cannot be null");

        // Obtener cliente existente
        Customer customer = customerRepository.findByIdentification(invoiceRequest.getCustomer().getIdentification());
        if (customer == null) {
            throw new IllegalArgumentException("Cliente con identificación " + invoiceRequest.getCustomer().getIdentification() + " no encontrado");
        }


        // Buscar o crear el cliente
        Customer customer1 = customerRepository.findByIdentification(invoiceRequest.getCustomer().getIdentification());
        if (customer == null) {
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

            // Configurar relaciones
            if (invoiceRequest.getCustomer().getLegalOrganizationId() != null) {
                Integer legalOrgId = Integer.valueOf(invoiceRequest.getCustomer().getLegalOrganizationId());

                LegalOrganization legalOrganization = legalOrganizationRepository.findById(legalOrgId)
                        .orElseGet(() -> {
                            LegalOrganization newLegalOrganization = new LegalOrganization();
                            newLegalOrganization.setId(legalOrgId);
                            newLegalOrganization.setCode("DEFAULT_CODE"); // Ajusta si tienes el valor
                            newLegalOrganization.setName("DEFAULT_NAME"); // Ajusta si tienes el valor
                            return legalOrganizationRepository.save(newLegalOrganization);
                        });

                customer.setLegal_organization(legalOrganization);
            }

            if (invoiceRequest.getCustomer().getTributeId() != null) {
                Integer tributeId = Integer.valueOf(invoiceRequest.getCustomer().getTributeId());

                Tribute tribute = tributeRepository.findById(tributeId)
                        .orElseGet(() -> {
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
                            Municipality newMunicipality = new Municipality();
                            newMunicipality.setId(municipalityId);
                            newMunicipality.setCode("DEFAULT_CODE");
                            newMunicipality.setName("DEFAULT_NAME");
                            return MunicipalityRepository.save(newMunicipality);
                        });

                customer.setMunicipality(municipality);
            }

            customer = customerRepository.save(customer);
        }

        // Crear la factura
        Invoice invoice = new Invoice(); // Inicializamos invoice aquí
        invoice.setFactusInvoiceId(invoiceResponse.getData().getBill().getId());
        invoice.setInvoiceNumber(invoiceResponse.getData().getBill().getNumber());
        invoice.setInvoiceUuid(invoiceResponse.getData().getBill().getCufe());
        invoice.setStatus(invoiceResponse.getData().getBill().getStatus());
        invoice.setReferenceCode(invoiceRequest.getReference_code());
        invoice.setObservation(invoiceRequest.getObservation());
        if (invoiceRequest.getPayment_form() != null) {
            invoice.setPaymentForm(invoiceRequest.getPayment_form());
        }
        invoice.setPaymentDueDate(invoiceRequest.getPayment_due_date());
        invoice.setPaymentMethodCode(invoiceRequest.getPayment_method_code());
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setCustomer(customer);


        // Guardar los ítems de la factura
        List<InvoiceItem> items = new ArrayList<>();
        for (ItemDTO itemDTO : invoiceRequest.getItems()) {
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

            // Configurar relaciones
            if (itemDTO.getUnit_measure() != null) {
                UnitMeasure unitMeasure = new UnitMeasure();
                unitMeasure.setId(itemDTO.getUnit_measure().getId());
                unitMeasure.setCode(itemDTO.getUnit_measure().getCode());
                unitMeasure.setName(itemDTO.getUnit_measure().getName());
                item.setUnitMeasure(unitMeasure);
            }

            if (itemDTO.getStandard_code() != null) {
                StandardCode standardCode = new StandardCode();
                standardCode.setId(itemDTO.getStandard_code().getId());
                standardCode.setCode(itemDTO.getStandard_code().getCode());
                standardCode.setName(itemDTO.getStandard_code().getName());
                item.setStandardCode(standardCode);
            }

            if (itemDTO.getTribute() != null) {
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
                    WithholdingTax tax = new WithholdingTax();
                    tax.setInvoiceItem(item);
                    tax.setCode(taxDTO.getCode());
                    tax.setName(taxDTO.getName());
                    tax.setWithholdingTaxRate(taxDTO.getValue());

                    // Guardar las tasas de retención
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
        invoiceRepository.save(invoice);
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
}