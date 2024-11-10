package com.project.invoice_tracking_system.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.project.invoice_tracking_system.entities.Invoice;
import com.project.invoice_tracking_system.entities.Item;
import com.project.invoice_tracking_system.entities.User;
import com.project.invoice_tracking_system.repositories.InvoiceRepository;
import com.project.invoice_tracking_system.repositories.ItemRepository;
import com.project.invoice_tracking_system.repositories.UserRepository;

/**
 * The ItemService class is responsible for managing items in the invoice tracking system.
 * It provides functionality to retrieve all items, and find items that have not yet been added to a specific invoice.
 * 
 * The ItemService utilizes the following repositories:
 * - ItemRepository: For managing items in the system.
 * - UserRepository: For retrieving user-related information.
 * - InvoiceRepository: For managing invoices and tracking which items are associated with them.
 * 
 * @see ItemRepository
 * @see UserRepository
 * @see InvoiceRepository
 */
@Service
public class ItemService {
	@Autowired 
	private ItemRepository itemRepository;
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private InvoiceRepository invoiceRepository;
	
	 /**
     * Retrieves a list of all items available in the system.
     * This method fetches all items that can potentially be added to an invoice.
     * 
     * @return A list of all items available in the system.
     */
	public List<Item> getAllItems(){		
		 List<Item> items = new ArrayList<>();
		 itemRepository.findAll()
		 .forEach(items:: add);
			return items;	
	}
	
	/**
     * Retrieves a list of items that have not yet been added to a specific invoice.
     * This method checks which items are available in the system but are not currently associated with the provided invoice.
     * 
     * @param invoiceId The ID of the invoice for which unassociated items are to be retrieved.
     * @param email The email of the user who is requesting the items.
     * @return A list of items that are not currently part of the specified invoice.
     */
	public List<Item> findItemsNotInInvoice(int invoiceId, String email){			
		User user = userRepository.findByEmail(email).get();
		Invoice invoice = invoiceRepository.findById(invoiceId).get();
		if( (user.getRole().getName().equalsIgnoreCase("SUPERUSER")) || (user.getRole().getName().equalsIgnoreCase("AUDITOR")) || (invoice.getUser().getId() == user.getId()) ) {
			return  itemRepository.findItemsNotInInvoice(invoiceId);
		}
        throw new AccessDeniedException("Access Denied");
	}
	
}
