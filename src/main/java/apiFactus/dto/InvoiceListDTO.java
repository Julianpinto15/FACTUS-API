package apiFactus.dto;

import java.time.LocalDateTime;

public class InvoiceListDTO {
    private Long id;
    private String invoiceNumber;
    private String invoiceUuid;
    private CustomerDTO customer;
    private LocalDateTime createdAt;
    private String status;

    // Constructor vacío
    public InvoiceListDTO() {}

    // Constructor completo
    public InvoiceListDTO(Long id, String invoiceNumber, String invoiceUuid,
                          CustomerDTO customer, LocalDateTime createdAt, String status) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.invoiceUuid = invoiceUuid;
        this.customer = customer;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Getters y Setters
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

    // Clase interna CustomerDTO
    public static class CustomerDTO {
        private String company;
        private String names;

        public CustomerDTO() {}

        public CustomerDTO(String company, String names) {
            this.company = company;
            this.names = names;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getNames() {
            return names;
        }

        public void setNames(String names) {
            this.names = names;
        }
    }
}