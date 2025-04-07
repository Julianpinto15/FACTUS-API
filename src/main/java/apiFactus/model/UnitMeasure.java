package apiFactus.model;

import jakarta.persistence.*;

@Entity
public class UnitMeasure {

    @Id
    private Integer id;
    private String name;
    private String symbol;


    @Version
    private Long version = 0L;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

