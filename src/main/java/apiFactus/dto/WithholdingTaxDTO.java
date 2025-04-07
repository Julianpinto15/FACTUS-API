package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WithholdingTaxDTO {

    @JsonAlias({"tribute_code"})
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name; // Agregado

    @JsonAlias({"value"})
    @JsonProperty("withholding_tax_rate")
    private String value; // Cambiado de withholding_tax_rate a value

    public WithholdingTaxDTO() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Método para convertir value a Double si es necesario
    public Double getValueAsDouble() {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value format: " + value, e);
        }
    }
}