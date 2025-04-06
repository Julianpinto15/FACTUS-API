package apiFactus.dto;

public class InvoiceResponseDTO {

    private Boolean success;
    private String message;
    private InvoiceData data;


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
