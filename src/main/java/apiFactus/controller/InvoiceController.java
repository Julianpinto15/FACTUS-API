package apiFactus.controller;

import apiFactus.dto.InvoiceRequestDTO;
import apiFactus.dto.InvoiceResponseDTO;
import apiFactus.service.InvoiceService;
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
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody InvoiceRequestDTO invoiceRequestDTO) {
        InvoiceResponseDTO response = invoiceService.createInvoice(invoiceRequestDTO);
        return ResponseEntity.ok(response);
    }
}