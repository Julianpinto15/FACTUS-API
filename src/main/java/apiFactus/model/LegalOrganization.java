package apiFactus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "legal_organization")
public class LegalOrganization {

    @Id
    private Integer id;
    private String code;
    private String name;

    @Version
    private Long version = 0L;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "LegalOrganization{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }

}