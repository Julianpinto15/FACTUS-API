package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitMeasureDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("code")
    private String code;  // Agregado

    @JsonProperty("name")
    private String name;

    public UnitMeasureDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "UnitMeasureDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
