package apiFactus.repository;

import apiFactus.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByFactusInvoiceId(String factusInvoiceId);
    Invoice findByInvoiceNumber(String invoiceNumber);
}
