package apiFactus.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull; // Para Jakarta EE 9 o superior


public class CustomerDTO {

    private String id; // Para compatibilidad con el frontend, pero no se usa en Factus

    @NotNull(message = "identification_document_id es obligatorio")
    private Integer identification_document_id;

    @NotNull(message = "identification es obligatorio")
    private String identification;

    private Integer dv;

    private String graphic_representation_name;
    private String company;
    private String trade_name;

    @NotNull(message = "names es obligatorio")
    private String names;

    @NotNull(message = "address es obligatorio")
    private String address;

    @NotNull(message = "email es obligatorio")
    private String email;

    @NotNull(message = "phone es obligatorio")
    private String phone;

    @NotNull(message = "legal_organization_id es obligatorio")
    @JsonProperty("legal_organization_id")
    private String legalOrganizationId;

    @NotNull(message = "tribute_id es obligatorio")
    @JsonProperty("tribute_id")
    private String tributeId;

    @NotNull(message = "municipality_id es obligatorio")
    @JsonProperty("municipality_id")
    private String municipalityId;

    // Constructor vacío
    public CustomerDTO() {
    }

    // Constructor completo
    public CustomerDTO(Integer identification_document_id, String identification, Integer dv, String graphic_representation_name,
                       String company, String trade_name, String names, String address, String email, String phone,
                       String legalOrganizationId, String tributeId, String municipalityId) {
        this.identification_document_id = identification_document_id;
        this.identification = identification;
        this.dv = dv;
        this.graphic_representation_name = graphic_representation_name;
        this.company = company;
        this.trade_name = trade_name;
        this.names = names;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.legalOrganizationId = legalOrganizationId;
        this.tributeId = tributeId;
        this.municipalityId = municipalityId;
    }

    public String getId() {
        return identification;
    }

    public void setId(String id) {
        this.identification = id;
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

    public String getGraphic_representation_name() {
        return graphic_representation_name;
    }

    public void setGraphic_representation_name(String graphic_representation_name) {
        this.graphic_representation_name = graphic_representation_name;
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

    public String getLegalOrganizationId() {
        return legalOrganizationId;
    }

    public void setLegalOrganizationId(String legalOrganizationId) {
        this.legalOrganizationId = legalOrganizationId;
    }



    public String getTributeId() {
        return tributeId;
    }

    public void setTributeId(String tributeId) {
        this.tributeId = tributeId;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }


}