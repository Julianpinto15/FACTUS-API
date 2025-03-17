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

        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;

        @Column(name = "code_reference")
        private String codeReference;

        @Column(name = "name")
        private String name;

        @Column(name = "quantity")
        private Integer quantity;

        @Column(name = "discount_rate")
        private Integer discountRate;

        @Column(name = "price")
        private Integer price;

        @Column(name = "tax_rate")
        private String taxRate;

        @Column(name = "unit_measure_id")
        private Integer unitMeasureId;

        @Column(name = "standard_code_id")
        private Integer standardCodeId;

        @Column(name = "is_excluded")
        private Integer isExcluded;

        @Column(name = "tribute_id")
        private Integer tributeId;

        @OneToMany(mappedBy = "invoiceItem", cascade = CascadeType.ALL)
        private List<WithholdingTax> withholdingTaxes;


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

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
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

    public Integer getIsExcluded() {
        return isExcluded;
    }

    public void setIsExcluded(Integer isExcluded) {
        this.isExcluded = isExcluded;
    }

    public Integer getTributeId() {
        return tributeId;
    }

    public void setTributeId(Integer tributeId) {
        this.tributeId = tributeId;
    }

    public List<WithholdingTax> getWithholdingTaxes() {
        return withholdingTaxes;
    }

    public void setWithholdingTaxes(List<WithholdingTax> withholdingTaxes) {
        this.withholdingTaxes = withholdingTaxes;
    }
}
