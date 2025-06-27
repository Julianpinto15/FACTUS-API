package apiFactus.controller;

import apiFactus.dto.InvoiceRequestDTO;
import apiFactus.dto.InvoiceResponseDTO;
import apiFactus.dto.PaginatedResponse;
import apiFactus.model.Invoice;
import apiFactus.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/bills")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/validate")
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody InvoiceRequestDTO request) {
        logger.debug("Recibida solicitud para crear factura: reference_code={}", request.getReference_code());
        try {
            InvoiceResponseDTO response = invoiceService.createInvoice(request);
            if (response != null && "Created".equals(response.getStatus())) {
                logger.debug("Factura creada exitosamente: status_code=201, invoice_id={}",
                        response.getData() != null && response.getData().getBill() != null
                                ? response.getData().getBill().getId() : "N/A");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                logger.warn("Solicitud de factura inválida: status_code=400, message={}",
                        response != null ? response.getMessage() : "Respuesta nula");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (RuntimeException e) {
            logger.error("Error al crear la factura: status_code=500, message={}", e.getMessage(), e);
            InvoiceResponseDTO errorResponse = new InvoiceResponseDTO();
            errorResponse.setStatus("Error");
            errorResponse.setMessage("Error al crear la factura: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        logger.debug("Recibida solicitud para listar facturas");
        try {
            List<Invoice> invoices = invoiceService.getAllInvoices();
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            logger.error("Error al listar facturas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/validate/paginated")
    public ResponseEntity<PaginatedResponse> getPaginatedInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.debug("Recibida solicitud para listar facturas página {} con tamaño {}", page, size);
        try {
            PaginatedResponse response = invoiceService.getPaginatedInvoices(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al listar facturas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/show/{number}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceByNumber(@PathVariable String number) {
        try {
            InvoiceResponseDTO response = invoiceService.getInvoiceByNumber(number);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error fetching invoice: " + e.getMessage(), e);
            InvoiceResponseDTO errorResponse = new InvoiceResponseDTO();
            errorResponse.setStatus("Error");
            errorResponse.setMessage("Failed to fetch invoice: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/download-pdf/{number}")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable String number) {
        try {
            byte[] pdfBytes = invoiceService.downloadInvoicePdf(number);

            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.noContent().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("invoice_" + number + ".pdf")
                    .build());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            logger.error("Error downloading PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/download-xml/{number}")
    public ResponseEntity<byte[]> downloadInvoiceXml(@PathVariable String number) {
        try {
            byte[] xmlBytes = invoiceService.downloadInvoiceXml(number);
            if (xmlBytes == null || xmlBytes.length == 0) {
                logger.error("No content returned for XML download for invoice {}", number);
                return ResponseEntity.noContent().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("invoice_" + number + ".xml")
                    .build());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(xmlBytes);
        } catch (Exception e) {
            logger.error("Error downloading XML for invoice {}: {}", number, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}