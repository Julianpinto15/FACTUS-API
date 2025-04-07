package apiFactus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NumberingRangeDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("prefix")
    private String prefix;

    @JsonProperty("from")
    private Long from;  // Cambiado de start_number a from

    @JsonProperty("to")
    private Long to;  // Cambiado de end_number a to

    @JsonProperty("resolution_number")
    private String resolutionNumber;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("months")
    private Integer months;

    public NumberingRangeDTO() {
    }

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

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getResolutionNumber() {
        return resolutionNumber;
    }

    public void setResolutionNumber(String resolutionNumber) {
        this.resolutionNumber = resolutionNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    @Override
    public String toString() {
        return "NumberingRangeDTO{" +
                "prefix='" + prefix + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", resolutionNumber='" + resolutionNumber + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", months=" + months +
                '}';
    }

}
