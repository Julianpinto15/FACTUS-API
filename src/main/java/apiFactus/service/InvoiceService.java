package apiFactus.service;

import apiFactus.dto.InvoiceRequestDTO;
import apiFactus.dto.InvoiceResponseDTO;
import apiFactus.dto.ItemDTO;
import apiFactus.dto.WithholdingTaxDTO;
import apiFactus.model.Customer;
import apiFactus.model.Invoice;
import apiFactus.model.InvoiceItem;
import apiFactus.model.WithholdingTax;
import apiFactus.repository.CustomerRepository;
import apiFactus.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${factus.api.url}")
    private String apiUrl;

    @Autowired
    public InvoiceService(
            @Qualifier("apiRestTemplate") RestTemplate restTemplate,
            AuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    @Transactional
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken());

        // El token será añadido automáticamente por el interceptor
        HttpEntity<InvoiceRequestDTO> request = new HttpEntity<>(invoiceRequest, headers);

        try {
            ResponseEntity<InvoiceResponseDTO> response = restTemplate.postForEntity(
                    apiUrl + "/api/v1/invoice",
                    request,
                    InvoiceResponseDTO.class);

            if (response.getBody() != null && response.getBody().getSuccess()) {
                // Guardar la factura en la base de datos local
                saveInvoiceToDatabase(invoiceRequest, response.getBody());
            }

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            // Si hay error de autorización, refrescamos el token y lo intentamos de nuevo
            authService.refreshToken();
            headers.setBearerAuth(authService.getAccessToken());
            request = new HttpEntity<>(invoiceRequest, headers);

            ResponseEntity<InvoiceResponseDTO> response = restTemplate.postForEntity(
                    apiUrl + "/api/v1/invoice",
                    request,
                    InvoiceResponseDTO.class);

            if (response.getBody() != null && response.getBody().getSuccess()) {
                // Guardar la factura en la base de datos local
                saveInvoiceToDatabase(invoiceRequest, response.getBody());
            }

            return response.getBody();
        }
    }

    private void saveInvoiceToDatabase(InvoiceRequestDTO invoiceRequest, InvoiceResponseDTO invoiceResponse) {
        // Buscar o crear el cliente
        Customer customer = customerRepository.findByIdentification(invoiceRequest.getCustomer().getIdentification());
        if (customer == null) {
            customer = new Customer();
            customer.setIdentification(invoiceRequest.getCustomer().getIdentification());
            customer.setDv(invoiceRequest.getCustomer().getDv());
            customer.setCompany(invoiceRequest.getCustomer().getCompany());
            customer.setTradeName(invoiceRequest.getCustomer().getTrade_name());
            customer.setNames(invoiceRequest.getCustomer().getNames());
            customer.setAddress(invoiceRequest.getCustomer().getAddress());
            customer.setEmail(invoiceRequest.getCustomer().getEmail());
            customer.setPhone(invoiceRequest.getCustomer().getPhone());
            customer.setLegalOrganizationId(invoiceRequest.getCustomer().getLegal_organization_id());
            customer.setTributeId(invoiceRequest.getCustomer().getTribute_id());
            customer.setIdentificationDocumentId(invoiceRequest.getCustomer().getIdentification_document_id());
            customer.setMunicipalityId(invoiceRequest.getCustomer().getMunicipality_id());

            customer = customerRepository.save(customer);
        }

        // Crear la factura
        Invoice invoice = new Invoice();
        invoice.setFactusInvoiceId(invoiceResponse.getData().getId());
        invoice.setInvoiceNumber(invoiceResponse.getData().getInvoice_number());
        invoice.setInvoiceUuid(invoiceResponse.getData().getInvoice_uuid());
        invoice.setStatus(invoiceResponse.getData().getStatus());
        invoice.setReferenceCode(invoiceRequest.getReference_code());
        invoice.setObservation(invoiceRequest.getObservation());
        invoice.setPaymentForm(invoiceRequest.getPayment_form());
        invoice.setPaymentDueDate(invoiceRequest.getPayment_due_date());
        invoice.setPaymentMethodCode(invoiceRequest.getPayment_method_code());
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setCustomer(customer);

        // Datos del periodo de facturación
        if (invoiceRequest.getBilling_period() != null) {
            invoice.setBillingStartDate(invoiceRequest.getBilling_period().getStart_date());
            invoice.setBillingStartTime(invoiceRequest.getBilling_period().getStart_time());
            invoice.setBillingEndDate(invoiceRequest.getBilling_period().getEnd_date());
            invoice.setBillingEndTime(invoiceRequest.getBilling_period().getEnd_time());
        }

        invoice = invoiceRepository.save(invoice);

        // Guardar los ítems de la factura
        List<InvoiceItem> items = new ArrayList<>();
        for (ItemDTO itemDTO : invoiceRequest.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setCodeReference(itemDTO.getCode_reference());
            item.setName(itemDTO.getName());
            item.setQuantity(itemDTO.getQuantity());
            item.setDiscountRate(itemDTO.getDiscount_rate());
            item.setPrice(itemDTO.getPrice());
            item.setTaxRate(itemDTO.getTax_rate());
            item.setUnitMeasureId(itemDTO.getUnit_measure_id());
            item.setStandardCodeId(itemDTO.getStandard_code_id());
            item.setIsExcluded(itemDTO.getIs_excluded());
            item.setTributeId(itemDTO.getTribute_id());

            // Guardar las retenciones
            List<WithholdingTax> withholdingTaxes = new ArrayList<>();
            if (itemDTO.getWithholding_taxes() != null) {
                for (WithholdingTaxDTO taxDTO : itemDTO.getWithholding_taxes()) {
                    WithholdingTax tax = new WithholdingTax();
                    tax.setInvoiceItem(item);
                    tax.setCode(taxDTO.getCode());
                    tax.setWithholdingTaxRate(taxDTO.getWithholding_tax_rate());
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
        headers.setBearerAuth(authService.getAccessToken());

        // El token será añadido automáticamente por el interceptor
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<InvoiceResponseDTO> response = restTemplate.exchange(
                    apiUrl + "/api/v1/invoice/" + invoiceId + "/validation",
                    HttpMethod.POST,
                    request,
                    InvoiceResponseDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            // Si hay error de autorización, refrescamos el token y lo intentamos de nuevo
            authService.refreshToken();
            headers.setBearerAuth(authService.getAccessToken());
            request = new HttpEntity<>(headers);

            ResponseEntity<InvoiceResponseDTO> response = restTemplate.exchange(
                    apiUrl + "/api/v1/invoice/" + invoiceId + "/validation",
                    HttpMethod.POST,
                    request,
                    InvoiceResponseDTO.class);

            return response.getBody();
        }
    }
}
