package apiFactus.dto;

import java.util.List;

public class InvoiceRequestDTO {

    private Integer numbering_range_id;
    private String reference_code;
    private String observation;
    private Integer payment_method_code;
    private String payment_due_date;
    private PaymentFormDTO payment_form;
    private BillingPeriodDTO billing_period;
    private CustomerDTO customer;
    private List<ItemDTO> items;

    public InvoiceRequestDTO(Integer numbering_range_id, String reference_code, String observation, Integer payment_method_code, String payment_due_date, PaymentFormDTO payment_form, BillingPeriodDTO billing_period, CustomerDTO customer, List<ItemDTO> items) {
        this.numbering_range_id = numbering_range_id;
        this.reference_code = reference_code;
        this.observation = observation;
        this.payment_method_code = payment_method_code;
        this.payment_due_date = payment_due_date;
        this.payment_form = payment_form;
        this.billing_period = billing_period;
        this.customer = customer;
        this.items = items;
    }

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

    public Integer getPayment_method_code() {
        return payment_method_code;
    }

    public void setPayment_method_code(Integer payment_method_code) {
        this.payment_method_code = payment_method_code;
    }

    public String getPayment_due_date() {
        return payment_due_date;
    }

    public void setPayment_due_date(String payment_due_date) {
        this.payment_due_date = payment_due_date;
    }

    public PaymentFormDTO getPayment_form() {
        return payment_form;
    }

    public void setPayment_form(PaymentFormDTO payment_form) {
        this.payment_form = payment_form;
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
