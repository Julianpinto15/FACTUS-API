package apiFactus.dto;

public class InvoiceData {

    private Integer id;
    private String invoice_number;
    private String invoice_uuid;
    private String status;
    private String creation_date;

    public InvoiceData(Integer id, String invoice_number, String invoice_uuid, String status, String creation_date) {
        this.id = id;
        this.invoice_number = invoice_number;
        this.invoice_uuid = invoice_uuid;
        this.status = status;
        this.creation_date = creation_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_uuid() {
        return invoice_uuid;
    }

    public void setInvoice_uuid(String invoice_uuid) {
        this.invoice_uuid = invoice_uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }
}
