package apiFactus.dto;

public class WithholdingTaxDTO {

    private String code;
    private Double withholding_tax_rate;

    public WithholdingTaxDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getWithholding_tax_rate() {
        return withholding_tax_rate;
    }

    public void setWithholding_tax_rate(Double withholding_tax_rate) {
        this.withholding_tax_rate = withholding_tax_rate;
    }
}

