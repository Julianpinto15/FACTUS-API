package apiFactus.controller;

import apiFactus.dto.InvoiceRequestDTO;
import apiFactus.dto.InvoiceResponseDTO;
import apiFactus.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody InvoiceRequestDTO invoiceRequest) {
        InvoiceResponseDTO response = invoiceService.createInvoice(invoiceRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<InvoiceResponseDTO> validateInvoice(@PathVariable Integer id) {
        InvoiceResponseDTO response = invoiceService.validateInvoice(id);
        return ResponseEntity.ok(response);
    }
}