package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceResponseDTO {

    private Boolean success;
    private String message;
    @JsonProperty("data")
    private InvoiceData data;

    public InvoiceResponseDTO(Boolean success, String message, InvoiceData data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
}
