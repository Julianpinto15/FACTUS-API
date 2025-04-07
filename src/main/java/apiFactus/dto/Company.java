package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Company {

    @JsonProperty("url_logo")
    private String urlLogo;

    @JsonProperty("nit")
    private String nit;

    @JsonProperty("dv")
    private String dv;

    @JsonProperty("company")
    private String company;

    @JsonProperty("name")
    private String name;

    @JsonProperty("graphic_representation_name")
    private String graphicRepresentationName;

    @JsonProperty("registration_code")
    private String registrationCode;

    @JsonProperty("economic_activity")
    private String economicActivity;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("municipality")
    private String municipality;

    public Company() {
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphicRepresentationName() {
        return graphicRepresentationName;
    }

    public void setGraphicRepresentationName(String graphicRepresentationName) {
        this.graphicRepresentationName = graphicRepresentationName;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getEconomicActivity() {
        return economicActivity;
    }

    public void setEconomicActivity(String economicActivity) {
        this.economicActivity = economicActivity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
}
