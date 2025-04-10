package apiFactus.dto;

import apiFactus.model.UnitMeasure;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.util.List;

public class ItemDTO {

    private String code_reference;
    private String name;
    private Integer quantity;
    private Double discount_rate;
    @Column(name = "discount")
    private Double discount;

    @Column(name = "gross_value")
    private Double grossValue;

    @Column(name = "taxable_amount")
    private Double taxableAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @JsonProperty("standard_code_id")
    private Integer standardCodeId;

    @JsonProperty("unit_measure_id")
    private Integer unitMeasureId;

    private Double price;
    private Double tax_rate;
    private UnitMeasureDTO unit_measure;
    private StandardCodeDTO standard_code;
    private TributeDTO tribute;
    private Integer is_excluded;
    private Integer tribute_id;

    @Column(name = "total")
    private Double total;
    private List<WithholdingTaxDTO> withholding_taxes;


    public ItemDTO() {
    }

    public String getCode_reference() {
        return code_reference;
    }

    public void setCode_reference(String code_reference) {
        this.code_reference = code_reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(Double discount_rate) {
        this.discount_rate = discount_rate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(Double tax_rate) {
        this.tax_rate = tax_rate;
    }

    public UnitMeasureDTO getUnit_measure() {
        return unit_measure;
    }

    public void setUnit_measure(UnitMeasureDTO unit_measure) {
        this.unit_measure = unit_measure;
    }

    public StandardCodeDTO getStandard_code() {
        return standard_code;
    }

    public void setStandard_code(StandardCodeDTO standard_code) {
        this.standard_code = standard_code;
    }

    public TributeDTO getTribute() {
        return tribute;
    }

    public void setTribute(TributeDTO tribute) {
        this.tribute = tribute;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getIs_excluded() {
        return is_excluded;
    }

    public void setIs_excluded(Integer is_excluded) {
        this.is_excluded = is_excluded;
    }

    public Integer getTribute_id() {
        return tribute_id;
    }

    public void setTribute_id(Integer tribute_id) {
        this.tribute_id = tribute_id;
    }

    public List<WithholdingTaxDTO> getWithholding_taxes() {
        return withholding_taxes;
    }

    public void setWithholding_taxes(List<WithholdingTaxDTO> withholding_taxes) {
        this.withholding_taxes = withholding_taxes;
    }

    public Double getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(Double grossValue) {
        this.grossValue = grossValue;
    }

    public Double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(Double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getUnitMeasureId() {
        return unitMeasureId;
    }

    public void setUnitMeasureId(Integer unitMeasureId) {
        this.unitMeasureId = unitMeasureId;
    }

    public Integer getStandardCodeId() {
        return standardCodeId;
    }

    public void setStandardCodeId(Integer standardCodeId) {
        this.standardCodeId = standardCodeId;
    }
}
