package apiFactus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification_document_id")
    private Integer identificationDocumentId;

    @Column(name = "identification", nullable = false)
    private String identification;

    @Column(name = "dv")
    private Integer dv;

    @Column(name = "company")
    private String company;

    @Column(name = "trade_name")
    private String TradeName;

    @Column(name = "names")
    private String names;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "legal_organization_id")
    private Integer LegalOrganizationId;

    @Column(name = "tribute_id")
    private Integer TributeId;

    @Column(name = "municipality_id")
    private Integer MunicipalityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentificationDocumentId() {
        return identificationDocumentId;
    }

    public void setIdentificationDocumentId(Integer identificationDocumentId) {
        this.identificationDocumentId = identificationDocumentId;
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

    public String getTradeName() {
        return TradeName;
    }

    public void setTradeName(String tradeName) {
        TradeName = tradeName;
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

    public Integer getLegalOrganizationId() {
        return LegalOrganizationId;
    }

    public void setLegalOrganizationId(Integer legalOrganizationId) {
        LegalOrganizationId = legalOrganizationId;
    }

    public Integer getTributeId() {
        return TributeId;
    }

    public void setTributeId(Integer tributeId) {
        TributeId = tributeId;
    }

    public Integer getMunicipalityId() {
        return MunicipalityId;
    }

    public void setMunicipalityId(Integer municipalityId) {
        MunicipalityId = municipalityId;
    }
}
