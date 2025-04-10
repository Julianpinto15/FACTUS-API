package apiFactus.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Column(name = "code_reference")
    private String codeReference;

    private String name;

    private Integer quantity;

    @Column(name = "discount_rate")
    private Double discountRate;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "gross_value")
    private Double grossValue;

    @Column(name = "tax_rate")
    private Double taxRate; // Cambiado a Double

    @Column(name = "taxable_amount")
    private Double taxableAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    private Double price;

    @Column(name = "total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "unit_measure_id")
    private UnitMeasure unitMeasure;

    @ManyToOne
    @JoinColumn(name = "standard_code_id")
    private StandardCode standardCode;

    @Column(name = "is_excluded")
    private Integer isExcluded;

    @ManyToOne
    @JoinColumn(name = "tribute_id")
    private Tribute tribute;

    @OneToMany(mappedBy = "invoiceItem", cascade = CascadeType.ALL)
    private List<WithholdingTax> withholdingTaxes;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(Double grossValue) {
        this.grossValue = grossValue;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public UnitMeasure getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(UnitMeasure unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public StandardCode getStandardCode() {
        return standardCode;
    }

    public void setStandardCode(StandardCode standardCode) {
        this.standardCode = standardCode;
    }

    public Integer getIsExcluded() {
        return isExcluded;
    }

    public void setIsExcluded(Integer isExcluded) {
        this.isExcluded = isExcluded;
    }

    public Tribute getTribute() {
        return tribute;
    }

    public void setTribute(Tribute tribute) {
        this.tribute = tribute;
    }

    public List<WithholdingTax> getWithholdingTaxes() {
        return withholdingTaxes;
    }

    public void setWithholdingTaxes(List<WithholdingTax> withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }
}