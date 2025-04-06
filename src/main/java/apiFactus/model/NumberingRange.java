package apiFactus.model;

import jakarta.persistence.*;

@Entity
public class NumberingRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String prefix;
    private Long start_number;
    private Long end_number;
    private String status;

    @Version
    private Long version = 0L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getStart_number() {
        return start_number;
    }

    public void setStart_number(Long start_number) {
        this.start_number = start_number;
    }

    public Long getEnd_number() {
        return end_number;
    }

    public void setEnd_number(Long end_number) {
        this.end_number = end_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
