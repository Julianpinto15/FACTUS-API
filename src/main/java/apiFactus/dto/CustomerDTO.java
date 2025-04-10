package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerDTO {

    private Integer identification_document_id;
    private String identification;
    private Integer dv;
    private String graphic_representation_name;
    private String company;
    private String trade_name;
    private String names;
    private String address;
    private String email;
    private String phone;

    @JsonProperty("legal_organization_id")
    private String legalOrganizationId;

    @JsonProperty("tribute_id")
    private String tributeId;

    @JsonProperty("municipality_id")
    private String municipalityId;

    @JsonIgnore
    private LegalOrganizationDTO legal_organization;

    private TributeDTO tribute;
    private MunicipalityDTO municipality;

    public CustomerDTO() {
    }

    public CustomerDTO(Integer identification_document_id, String identification, Integer dv, String graphic_representation_name, String company, String trade_name, String names, String address, String email, String phone, String legalOrganizationId, String tributeId, String municipalityId, LegalOrganizationDTO legal_organization, TributeDTO tribute, MunicipalityDTO municipality) {
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
        this.legal_organization = legal_organization;
        this.tribute = tribute;
        this.municipality = municipality;
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

    // Getter que convierte el ID en objeto
    public LegalOrganizationDTO getLegal_organization() {
        if (legal_organization == null && legalOrganizationId != null) {
            legal_organization = new LegalOrganizationDTO();
            legal_organization.setId(Integer.parseInt(legalOrganizationId));
        }
        return legal_organization;
    }

    public void setLegal_organization(LegalOrganizationDTO legal_organization) {
        this.legal_organization = legal_organization;
    }

    public TributeDTO getTribute() {
        if (tribute == null && tributeId != null) {
            tribute = new TributeDTO();
            tribute.setId(Integer.parseInt(tributeId));
        }
        return tribute;
    }

    public void setTribute(TributeDTO tribute) {
        this.tribute = tribute;
    }

    public MunicipalityDTO getMunicipality() {
        if (municipality == null && municipalityId != null) {
            municipality = new MunicipalityDTO();
            municipality.setId(Integer.parseInt(municipalityId));
        }
        return municipality;
    }

    public void setMunicipality(MunicipalityDTO municipality) {
        this.municipality = municipality;
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