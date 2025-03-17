package apiFactus.dto;

public class WithholdingTaxDTO {

    private String code;
    private String withholding_tax_rate;

    public WithholdingTaxDTO(String code, String withholding_tax_rate) {
        this.code = code;
        this.withholding_tax_rate = withholding_tax_rate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWithholding_tax_rate() {
        return withholding_tax_rate;
    }

    public void setWithholding_tax_rate(String withholding_tax_rate) {
        this.withholding_tax_rate = withholding_tax_rate;
    }
}

