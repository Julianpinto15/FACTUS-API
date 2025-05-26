package apiFactus.controller;

import apiFactus.dto.CustomerDTO;
import apiFactus.model.Customer;
import apiFactus.repository.CustomerRepository;
import apiFactus.repository.LegalOrganizationRepository;
import apiFactus.repository.MunicipalityRepository;
import apiFactus.repository.TributeRepository;
import apiFactus.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(customerService.getAllCustomers(page, size));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping("/{identification}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable String identification) {
        CustomerDTO customerDTO = customerService.getCustomerByIdentification(identification);
        return ResponseEntity.ok(customerDTO);
    }
}