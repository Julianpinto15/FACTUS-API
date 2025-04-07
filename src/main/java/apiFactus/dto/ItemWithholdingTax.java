package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ItemWithholdingTax {

    @JsonProperty("tribute_code")
    private String tributeCode;

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    @JsonProperty("rates")
    private List<Rate> rates;

    public ItemWithholdingTax() {
    }

    public String getTributeCode() {
        return tributeCode;
    }

    public void setTributeCode(String tributeCode) {
        this.tributeCode = tributeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}