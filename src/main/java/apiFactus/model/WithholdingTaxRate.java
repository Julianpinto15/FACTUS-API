package apiFactus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "withholding_tax_rates")
public class WithholdingTaxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "withholding_tax_id")
    private WithholdingTax withholdingTax;

    private String code;

    private String name;

    private String rate;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WithholdingTax getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(WithholdingTax withholdingTax) {
        this.withholdingTax = withholdingTax;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}