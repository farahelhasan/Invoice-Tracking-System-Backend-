package com.project.invoice_tracking_system.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.project.invoice_tracking_system.entities.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer>  {
    Page<Invoice> findAll(Pageable pageable);
    Page<Invoice> findAllByUserId(int id, Pageable pageable);
    
    @Query("SELECT i FROM Invoice i WHERE i.user.fullName LIKE %:searchTerm% OR i.id = :invoiceId")
    Page<Invoice> searchByUsernameOrInvoiceId(@Param("searchTerm") String searchTerm, @Param("invoiceId") Long invoiceId, Pageable pageable);
}
