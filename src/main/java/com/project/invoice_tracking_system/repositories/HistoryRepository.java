package com.project.invoice_tracking_system.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.project.invoice_tracking_system.entities.History;

public interface HistoryRepository extends CrudRepository<History, Integer>{
	
    History findByInvoiceIdAndStatusAndItemId(int invoiceId, int status, int itemId);
    List <History> findAllByInvoiceId(int invoiceId);
    

}
