package apiFactus.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;



@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    private String graphic_representation_name;

    @Column(name = "names")
    private String names;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Version
    private Long version = 0L;

    @ManyToOne
    @JoinColumn(name = "legal_organization_id")
    private LegalOrganization legal_organization;

    @ManyToOne
    @JoinColumn(name = "tribute_id")
    private Tribute tribute;

    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getGraphic_representation_name() {
        return graphic_representation_name;
    }

    public void setGraphic_representation_name(String graphic_representation_name) {
        this.graphic_representation_name = graphic_representation_name;
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

    public LegalOrganization getLegal_organization() {
        return legal_organization;
    }

    public void setLegal_organization(LegalOrganization legal_organization) {
        this.legal_organization = legal_organization;
    }

    public Tribute getTribute() {
        return tribute;
    }

    public void setTribute(Tribute tribute) {
        this.tribute = tribute;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}