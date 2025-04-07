package apiFactus.dto;

public class PaymentFormDTO {

    private String code;

    public PaymentFormDTO() {
    }

    public PaymentFormDTO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

