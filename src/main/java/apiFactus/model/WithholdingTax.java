package apiFactus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "withholding_taxes")
public class WithholdingTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_item_id")
    private InvoiceItem invoiceItem;

    @JsonProperty("name")
    private String name;

    private String code;

    @Column(name = "withholding_tax_rate")
    private String withholdingTaxRate;

    @OneToMany(mappedBy = "withholdingTax", cascade = CascadeType.ALL)
    private List<WithholdingTaxRate> rates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvoiceItem getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWithholdingTaxRate() {
        return withholdingTaxRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setWithholdingTaxRate(String withholdingTaxRate) {
        this.withholdingTaxRate = withholdingTaxRate;
    }

    public List<WithholdingTaxRate> getRates() {
        return rates;
    }

    public void setRates(List<WithholdingTaxRate> rates) {
        this.rates = rates;
    }
}
