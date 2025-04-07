package apiFactus.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "withholding_taxes")
@Data
public class WithholdingTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_item_id")
    private InvoiceItem invoiceItem;

    private String code;

    @Column(name = "withholding_tax_rate")
    private String withholdingTaxRate; // Cambiado a Double

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

    public void setWithholdingTaxRate(String withholdingTaxRate) {
        this.withholdingTaxRate = withholdingTaxRate;
    }
}