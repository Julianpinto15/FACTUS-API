package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceResponseDTO {

    @JsonProperty("status")  // Cambia "success" por "status" y usa String
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private InvoiceData data;

    @JsonProperty("billing_period")
    private Object billingPeriod;

    public InvoiceResponseDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InvoiceData getData() {
        return data;
    }

    public void setData(InvoiceData data) {
        this.data = data;
    }

    public Object getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(Object billingPeriod) {
        this.billingPeriod = billingPeriod;
    }
}
