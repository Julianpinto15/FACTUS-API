package apiFactus.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification", nullable = false)
    private String identification;

    @Column(name = "dv")
    private String dv;

    @Column(name = "company")
    private String company;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "names")
    private String names;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "legal_organization_id")
    private String legalOrganizationId;

    @Column(name = "tribute_id")
    private String tributeId;

    @Column(name = "identification_document_id")
    private String identificationDocumentId;

    @Column(name = "municipality_id")
    private String municipalityId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
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

    public String getIdentificationDocumentId() {
        return identificationDocumentId;
    }

    public void setIdentificationDocumentId(String identificationDocumentId) {
        this.identificationDocumentId = identificationDocumentId;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }
}