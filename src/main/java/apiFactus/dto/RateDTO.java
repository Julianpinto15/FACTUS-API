package apiFactus.dto;

import apiFactus.model.WithholdingTax;

import java.util.List;

public class RateDTO {

    private String code;
    private String name;
    private String rate;

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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
