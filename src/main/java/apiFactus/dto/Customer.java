package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

    @JsonProperty("identification")
    private String identification;

    @JsonProperty("dv")
    private String dv;  // Puede ser null en la respuesta

    @JsonProperty("graphic_representation_name")
    private String graphicRepresentationName;

    @JsonProperty("trade_name")
    private String tradeName;

    @JsonProperty("company")
    private String company;

    @JsonProperty("names")
    private String names;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("legal_organization")
    private LegalOrganization legalOrganization;

    @JsonProperty("tribute")
    private TributeDTO tribute;

    @JsonProperty("municipality")
    private MunicipalityDTO municipality;

    public Customer() {
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getGraphicRepresentationName() {
        return graphicRepresentationName;
    }

    public void setGraphicRepresentationName(String graphicRepresentationName) {
        this.graphicRepresentationName = graphicRepresentationName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public LegalOrganization getLegalOrganization() {
        return legalOrganization;
    }

    public void setLegalOrganization(LegalOrganization legalOrganization) {
        this.legalOrganization = legalOrganization;
    }

    public TributeDTO getTribute() {
        return tribute;
    }

    public void setTribute(TributeDTO tribute) {
        this.tribute = tribute;
    }

    public MunicipalityDTO getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityDTO municipality) {
        this.municipality = municipality;
    }
}