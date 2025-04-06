package apiFactus.dto;

public class NumberingRangeDTO {
    private Integer id;
    private String prefix;
    private Long start_number;
    private Long end_number;
    private String status;

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

    @Override
    public String toString() {
        return "NumberingRangeDTO{" +
                "id=" + id +
                ", prefix='" + prefix + '\'' +
                ", start_number=" + start_number +
                ", end_number=" + end_number +
                ", status='" + status + '\'' +
                '}';
    }

}
