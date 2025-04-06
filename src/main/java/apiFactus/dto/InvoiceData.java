package apiFactus.dto;

public class InvoiceData {

    private String id;
    private String invoice_number;
    private String invoice_uuid;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
