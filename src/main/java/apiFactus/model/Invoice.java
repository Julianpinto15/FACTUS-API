package apiFactus.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "factus_invoice_id")
    private Integer factusInvoiceId;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "invoice_uuid")
    private String invoiceUuid;

    private Integer status;

    @Column(name = "reference_code")
    private String referenceCode;

    private String observation;

    @Column(name = "payment_form")
    private String paymentForm; // Cambiado a Integer para coincidir con PaymentFormDTO.code

    @Column(name = "payment_due_date")
    private String paymentDueDate;

    @Column(name = "payment_method_code")
    private Integer paymentMethodCode; // Cambiado a Integer para coincidir con InvoiceRequestDTO

    @Column(name = "billing_start_date")
    private String billingStartDate;

    @Column(name = "billing_start_time")
    private String billingStartTime;

    @Column(name = "billing_end_date")
    private String billingEndDate;

    @Column(name = "billing_end_time")
    private String billingEndTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String document;
    private Integer numberingRangeId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFactusInvoiceId() {
        return factusInvoiceId;
    }

    public void setFactusInvoiceId(Integer factusInvoiceId) {
        this.factusInvoiceId = factusInvoiceId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(String paymentForm) {
        this.paymentForm = paymentForm;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public Integer getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(Integer paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getBillingStartDate() {
        return billingStartDate;
    }

    public void setBillingStartDate(String billingStartDate) {
        this.billingStartDate = billingStartDate;
    }

    public String getBillingStartTime() {
        return billingStartTime;
    }

    public void setBillingStartTime(String billingStartTime) {
        this.billingStartTime = billingStartTime;
    }

    public String getBillingEndDate() {
        return billingEndDate;
    }

    public void setBillingEndDate(String billingEndDate) {
        this.billingEndDate = billingEndDate;
    }

    public String getBillingEndTime() {
        return billingEndTime;
    }

    public void setBillingEndTime(String billingEndTime) {
        this.billingEndTime = billingEndTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Integer getNumberingRangeId() {
        return numberingRangeId;
    }

    public void setNumberingRangeId(Integer numberingRangeId) {
        this.numberingRangeId = numberingRangeId;
    }
}