package apiFactus.dto;

import java.util.List;

public class ApiResponseDTO<T> {
    private String status;
    private List<T> data;

    public ApiResponseDTO() {
    }

    // Getters y setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}