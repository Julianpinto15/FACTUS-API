package apiFactus.dto;

public class CustomerDTO {

    private Integer identification_document_id;
    private String identification;
    private Integer dv;
    private String company;
    private String trade_name;
    private String names;
    private String address;
    private String email;
    private String phone;
    private Integer legal_organization_id;
    private Integer tribute_id;
    private Integer municipality_id;

    public CustomerDTO(Integer identification_document_id, String identification, Integer dv, String company, String trade_name, String names, String address, String email, String phone, Integer legal_organization_id, Integer tribute_id, Integer municipality_id) {
        this.identification_document_id = identification_document_id;
        this.identification = identification;
        this.dv = dv;
        this.company = company;
        this.trade_name = trade_name;
        this.names = names;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.legal_organization_id = legal_organization_id;
        this.tribute_id = tribute_id;
        this.municipality_id = municipality_id;
    }

    public Integer getIdentification_document_id() {
        return identification_document_id;
    }

    public void setIdentification_document_id(Integer identification_document_id) {
        this.identification_document_id = identification_document_id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public Integer getDv() {
        return dv;
    }

    public void setDv(Integer dv) {
        this.dv = dv;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTrade_name() {
        return trade_name;
    }

    public void setTrade_name(String trade_name) {
        this.trade_name = trade_name;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLegal_organization_id() {
        return legal_organization_id;
    }

    public void setLegal_organization_id(Integer legal_organization_id) {
        this.legal_organization_id = legal_organization_id;
    }

    public Integer getTribute_id() {
        return tribute_id;
    }

    public void setTribute_id(Integer tribute_id) {
        this.tribute_id = tribute_id;
    }

    public Integer getMunicipality_id() {
        return municipality_id;
    }

    public void setMunicipality_id(Integer municipality_id) {
        this.municipality_id = municipality_id;
    }
}