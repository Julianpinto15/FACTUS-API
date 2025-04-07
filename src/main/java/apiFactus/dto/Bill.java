package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Bill {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("document")
    private Document document;

    @JsonProperty("number")
    private String number;

    @JsonProperty("reference_code")
    private String referenceCode;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("send_email")
    private Integer sendEmail;

    @JsonProperty("qr")
    private String qr;

    @JsonProperty("cufe")
    private String cufe;

    @JsonProperty("validated")
    private String validated;

    @JsonProperty("discount_rate")
    private String discountRate;

    @JsonProperty("discount")
    private String discount;

    @JsonProperty("gross_value")
    private String grossValue;

    @JsonProperty("taxable_amount")
    private String taxableAmount;

    @JsonProperty("tax_amount")
    private String taxAmount;

    @JsonProperty("total")
    private String total;

    @JsonProperty("observation")
    private String observation;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("payment_due_date")
    private String paymentDueDate;

    @JsonProperty("qr_image")
    private String qrImage;

    @JsonProperty("has_claim")
    private Integer hasClaim;

    @JsonProperty("is_negotiable_instrument")
    private Integer isNegotiableInstrument;

    @JsonProperty("payment_form")
    private PaymentFormDTO paymentForm;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("public_url")
    private String publicUrl;

    // Añadir el campo billing_period como una lista
    @JsonProperty("billing_period")
    private List<BillingPeriodDTO> billing_period;

    public Bill() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Integer sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getCufe() {
        return cufe;
    }

    public void setCufe(String cufe) {
        this.cufe = cufe;
    }

    public String getValidated() {
        return validated;
    }

    public void setValidated(String validated) {
        this.validated = validated;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(String grossValue) {
        this.grossValue = grossValue;
    }

    public String getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(String taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getQrImage() {
        return qrImage;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    public Integer getHasClaim() {
        return hasClaim;
    }

    public void setHasClaim(Integer hasClaim) {
        this.hasClaim = hasClaim;
    }

    public Integer getIsNegotiableInstrument() {
        return isNegotiableInstrument;
    }

    public void setIsNegotiableInstrument(Integer isNegotiableInstrument) {
        this.isNegotiableInstrument = isNegotiableInstrument;
    }

    public PaymentFormDTO getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(PaymentFormDTO paymentForm) {
        this.paymentForm = paymentForm;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public List<BillingPeriodDTO> getBilling_period() {
        return billing_period;
    }

    public void setBilling_period(List<BillingPeriodDTO> billing_period) {
        this.billing_period = billing_period;
    }
}