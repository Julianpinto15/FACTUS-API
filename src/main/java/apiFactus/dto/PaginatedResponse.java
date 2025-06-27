package apiFactus.dto;

import java.util.List;

public class PaginatedResponse<T> {

    private List<T> data;
    private Long total;

    public PaginatedResponse(List<T> data, Long total) {
        this.data = data;
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}