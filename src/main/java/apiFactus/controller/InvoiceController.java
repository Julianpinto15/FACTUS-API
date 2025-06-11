package apiFactus.controller;

import apiFactus.dto.InvoiceRequestDTO;
import apiFactus.dto.InvoiceResponseDTO;
import apiFactus.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/bills/validate")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody InvoiceRequestDTO request) {
        try {
            InvoiceResponseDTO response = invoiceService.createInvoice(request);
            if (response != null && "Created".equals(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
            }
        } catch (RuntimeException e) {
            InvoiceResponseDTO errorResponse = new InvoiceResponseDTO();
            errorResponse.setStatus("Error");
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // 500 Internal Server Error
        }
    }
}