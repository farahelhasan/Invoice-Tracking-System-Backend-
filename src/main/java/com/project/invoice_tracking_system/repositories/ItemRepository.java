package com.project.invoice_tracking_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.project.invoice_tracking_system.entities.Item;

public interface ItemRepository extends CrudRepository<Item, Integer> {

	 @Query("SELECT i FROM Item i WHERE i.id NOT IN (SELECT ii.item.id FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId)")
	    List<Item> findItemsNotInInvoice(int invoiceId);
}
