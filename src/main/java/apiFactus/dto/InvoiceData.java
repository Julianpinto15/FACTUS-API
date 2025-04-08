package apiFactus.dto;

import apiFactus.model.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.List;

public class InvoiceData {

    @JsonProperty("company")
    private Company company;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("numbering_range")
    private NumberingRangeDTO numberingRange;

    @JsonProperty("billing_period")
    private Object billingPeriod;

    @JsonProperty("bill")
    private Bill bill;

    @JsonProperty("related_documents")
    private List<Object> relatedDocuments;

    @JsonProperty("items")
    private List<ItemDTO> items;

    @JsonProperty("withholding_taxes")
    private List<WithholdingTaxDTO> withholdingTaxes;

    @JsonProperty("credit_notes")
    private List<Object> creditNotes;

    @JsonProperty("debit_notes")
    private List<Object> debitNotes;

    public InvoiceData() {
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public NumberingRangeDTO getNumberingRange() {
        return numberingRange;
    }

    public void setNumberingRange(NumberingRangeDTO numberingRange) {
        this.numberingRange = numberingRange;
    }

    public Object getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(Object billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<Object> getRelatedDocuments() {
        return relatedDocuments;
    }

    public void setRelatedDocuments(List<Object> relatedDocuments) {
        this.relatedDocuments = relatedDocuments;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public List<WithholdingTaxDTO> getWithholdingTaxes() {
        return withholdingTaxes;
    }

    public void setWithholdingTaxes(List<WithholdingTaxDTO> withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }

    public List<Object> getCreditNotes() {
        return creditNotes;
    }

    public void setCreditNotes(List<Object> creditNotes) {
        this.creditNotes = creditNotes;
    }

    public List<Object> getDebitNotes() {
        return debitNotes;
    }

    public void setDebitNotes(List<Object> debitNotes) {
        this.debitNotes = debitNotes;
    }


    // Métodos auxiliares para manejar billing_period
    public List<BillingPeriodDTO> getBillingPeriodAsList() {
        if (billingPeriod instanceof List) {
            return (List<BillingPeriodDTO>) billingPeriod;
        }
        return null;
    }

    public BillingPeriodDTO getBillingPeriodAsObject() {
        if (billingPeriod instanceof BillingPeriodDTO) {
            return (BillingPeriodDTO) billingPeriod;
        } else if (billingPeriod instanceof java.util.Map) {
            // Convertir manualmente un Map (objeto JSON) a BillingPeriodDTO
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(billingPeriod, BillingPeriodDTO.class);
        }
        return null;
    }

}
