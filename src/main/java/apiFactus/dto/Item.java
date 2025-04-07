package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Item {

    @JsonProperty("code_reference")
    private String codeReference;

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("discount_rate")
    private String discountRate;

    @JsonProperty("discount")
    private String discount;

    @JsonProperty("gross_value")
    private String grossValue;

    @JsonProperty("tax_rate")
    private String taxRate;

    @JsonProperty("taxable_amount")
    private String taxableAmount;

    @JsonProperty("tax_amount")
    private String taxAmount;

    @JsonProperty("price")
    private String price;

    @JsonProperty("is_excluded")
    private Integer isExcluded;

    @JsonProperty("unit_measure")
    private UnitMeasureDTO unitMeasure;

    @JsonProperty("standard_code")
    private StandardCode standardCode;

    @JsonProperty("tribute")
    private TributeDTO tribute;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("withholding_taxes")
    private List<ItemWithholdingTax> withholdingTaxes;

    public Item() {
    }

    public String getCodeReference() {
        return codeReference;
    }

    public void setCodeReference(String codeReference) {
        this.codeReference = codeReference;
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

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(String grossValue) {
        this.grossValue = grossValue;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(String taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getIsExcluded() {
        return isExcluded;
    }

    public void setIsExcluded(Integer isExcluded) {
        this.isExcluded = isExcluded;
    }

    public UnitMeasureDTO getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(UnitMeasureDTO unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public StandardCode getStandardCode() {
        return standardCode;
    }

    public void setStandardCode(StandardCode standardCode) {
        this.standardCode = standardCode;
    }

    public TributeDTO getTribute() {
        return tribute;
    }

    public void setTribute(TributeDTO tribute) {
        this.tribute = tribute;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ItemWithholdingTax> getWithholdingTaxes() {
        return withholdingTaxes;
    }

    public void setWithholdingTaxes(List<ItemWithholdingTax> withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }
}