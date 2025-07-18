package apiFactus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "products")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "tax_rate")
    private String taxRate;

    @Column(name = "unit_measure_id")
    private Integer unitMeasureId;

    @Column(name = "standard_code_id")
    private Integer standardCodeId;

    @Column(name = "is_excluded")
    private Boolean isExcluded;

    @Column(name = "tribute_id")
    private Integer tributeId;

    @Column(name = "active")
    private Boolean active = true;

    @Column(nullable = false)
    private Integer quantity = 1; // Nuevo campo

    @Column(name = "discount_rate", nullable = false)
    private Double discountRate = 0.0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
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

    public Boolean getExcluded() {
        return isExcluded;
    }

    public void setExcluded(Boolean excluded) {
        isExcluded = excluded;
    }

    public Integer getTributeId() {
        return tributeId;
    }

    public void setTributeId(Integer tributeId) {
        this.tributeId = tributeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
