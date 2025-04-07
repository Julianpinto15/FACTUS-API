package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentFormDTO {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

