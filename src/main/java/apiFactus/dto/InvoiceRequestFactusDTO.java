package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InvoiceRequestFactusDTO {

    @JsonProperty("numbering_range_id")
    private Integer numbering_range_id;

    @JsonProperty("reference_code")
    private String reference_code;

    @JsonProperty("observation")
    private String observation;

    @JsonProperty("payment_form")
    private String payment_form;

    @JsonProperty("payment_due_date")
    private String payment_due_date;

    @JsonProperty("payment_method_code")
    private String payment_method_code;

    @JsonProperty("billing_period")
    private BillingPeriodDTO billing_period;  // Objeto, como espera el API de Factus

    @JsonProperty("customer")
    private CustomerDTO customer;

    @JsonProperty("items")
    private List<ItemDTO> items;

    // Getters y setters
    public Integer getNumbering_range_id() {
        return numbering_range_id;
    }

    public void setNumbering_range_id(Integer numbering_range_id) {
        this.numbering_range_id = numbering_range_id;
    }

    public String getReference_code() {
        return reference_code;
    }

    public void setReference_code(String reference_code) {
        this.reference_code = reference_code;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getPayment_form() {
        return payment_form;
    }

    public void setPayment_form(String payment_form) {
        this.payment_form = payment_form;
    }

    public String getPayment_due_date() {
        return payment_due_date;
    }

    public void setPayment_due_date(String payment_due_date) {
        this.payment_due_date = payment_due_date;
    }

    public String getPayment_method_code() {
        return payment_method_code;
    }

    public void setPayment_method_code(String payment_method_code) {
        this.payment_method_code = payment_method_code;
    }

    public BillingPeriodDTO getBilling_period() {
        return billing_period;
    }

    public void setBilling_period(BillingPeriodDTO billing_period) {
        this.billing_period = billing_period;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}