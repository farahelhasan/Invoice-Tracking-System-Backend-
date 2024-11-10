package com.project.invoice_tracking_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.project.invoice_tracking_system.entities.InvoiceItem;

public interface InvoiceItemRepository extends CrudRepository<InvoiceItem, Integer> {

	Optional<InvoiceItem> findByInvoiceIdAndItemId(int invoiceId, int itemId);
	Optional<InvoiceItem> findByInvoiceId(int invoiceId);
	

    @Query("SELECT SUM(ii.item.price * ii.quantity) FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId")
    Double calculateTotalPriceByInvoiceId(int invoiceId);


}
