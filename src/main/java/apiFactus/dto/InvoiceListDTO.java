package apiFactus.dto;

import java.time.LocalDateTime;

public class InvoiceListDTO {

    private Long id;
    private String invoiceNumber;
    private String invoiceUuid;
    private CustomerDTO customer; // Use a nested DTO for customer
    private LocalDateTime createdAt;
    private String status;

    public InvoiceListDTO(Long id, String invoiceNumber, String invoiceUuid, String customerName, LocalDateTime createdAt, String status) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.invoiceUuid = invoiceUuid;
        this.customer = new CustomerDTO(customerName); // Simplified; adjust as needed
        this.createdAt = createdAt;
        this.status = status;
    }

    // Nested CustomerDTO class
    public static class CustomerDTO {
        private String name;

        public CustomerDTO(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceUuid() {
        return invoiceUuid;
    }

    public void setInvoiceUuid(String invoiceUuid) {
        this.invoiceUuid = invoiceUuid;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
