package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentMethod {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    public PaymentMethod() {
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