package com.project.invoice_tracking_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.invoice_tracking_system.dots.InvoiceItemDto;
import com.project.invoice_tracking_system.entities.History;
import com.project.invoice_tracking_system.entities.Invoice;
import com.project.invoice_tracking_system.entities.InvoiceItem;
import com.project.invoice_tracking_system.entities.Item;
import com.project.invoice_tracking_system.entities.User;
import com.project.invoice_tracking_system.repositories.HistoryRepository;
import com.project.invoice_tracking_system.repositories.ItemRepository;

/**
 * Service class that handles the creation of history records for various invoice-related actions.
 * This includes actions such as creating, deleting, or editing invoices and items within invoices.
 * 
 * The HistoryService utilizes the following repositories:
 * - HistoryRepository: For creating, updating, and retrieving history records.
 * - ItemRepository: For managing the items associated with invoices, used to track item changes.
 * 
 * @see HistoryRepository
 * @see ItemRepository
 */
@Service
public class HistoryService {

	@Autowired
	private HistoryRepository historyRepository;
	@Autowired 
	private ItemRepository itemRepository;
	
	/**
	 * Creates a history record for the creation of an invoice, logging the invoice details user who own the invoice.
	 * 
	 * @param invoice The invoice that was created.
	 * @param user The user who created the invoice.
	 */
	public void CreateInvoiceHistoryRecoard(Invoice invoice, User user) {
		    History history = new History();
	        history.setInvoice(invoice);
	        history.setUser(user);
	        history.setAction("Created");
	        history.setDescription("Create new Invoice");
	        history.setType("Invoice");
	        history.setStatus(1);
            historyRepository.save(history); 
    }

	/**
	 * Creates a history record for the deletion of an invoice, logging the invoice details user who own the invoice.
	 * 
	 * @param invoice The invoice that was deleted.
	 * @param user The user who deleted the invoice.
	 */
	public void CreateDeleteInvoiceHistoryRecoard(Invoice invoice, User user) {
			 
		    List<History> inactive = historyRepository.findAllByInvoiceId(invoice.getId());
	        for (History inactiveRecord : inactive) {
	        	inactiveRecord.setStatus(0);
	 	        historyRepository.save(inactiveRecord);
	        }   
		    History history = new History();
	        history.setInvoice(invoice);
	        history.setUser(user);
	        history.setAction("Deleted");
	        history.setDescription("Delete Invoice");
	        history.setType("Invoice");
	        history.setStatus(1);
         historyRepository.save(history);
         
         
 }
	
	/**
	 * Creates a history record for adding an item to an invoice, logging the invoice, item details, user who own the invoice.
	 * 
	 * @param invoice The invoice to which the item was added.
	 * @param invoiceItemDto The details of the item added to the invoice.
	 * @param user The user who added the item to the invoice.
	 */
	public void CreateAddItemHistoryRecoard(Invoice invoice, InvoiceItemDto invoiceItemDto, User user) {
	    
		 Item item = itemRepository.findById(invoiceItemDto.getItemId()).get();
		 
         History inactive = historyRepository.findByInvoiceIdAndStatusAndItemId(invoice.getId(),1, item.getId());
         if(inactive != null) {
         inactive.setStatus(0);
         historyRepository.save(inactive);
         }
		 History history = new History();
	        history.setInvoice(invoice);
	        history.setUser(user);
	        history.setAction("Added");
	        history.setDescription("Add new Item");
	        history.setType("Item");
	        history.setStatus(1);
	        history.setItem(item);
	        history.setPrice(item.getPrice());
	        history.setQuantity(invoiceItemDto.getQuantity());
	        
         historyRepository.save(history);
	}
	
	/**
	 * Creates a history record for deleting an item from an invoice, logging the invoice, item details, and the user who own the invoice.
	 * 
	 * @param invoice The invoice from which the item was deleted.
	 * @param invoiceItem The item that was deleted from the invoice.
	 * @param user The user who deleted the item from the invoice.
	 */
	public void CreateDeleteItemHistoryRecoard(Invoice invoice, InvoiceItem invoiceItem, User user) {
	    
		 Item item = invoiceItem.getItem();

         History inactive = historyRepository.findByInvoiceIdAndStatusAndItemId(invoice.getId(),1, item.getId());

        inactive.setStatus(0);

        historyRepository.save(inactive);

		 History history = new History();
	        history.setInvoice(invoice);
	        history.setUser(user);
	        history.setAction("Deleted");
	        history.setDescription("Delete Item");
	        history.setType("Item");
	        history.setStatus(1);
	        history.setItem(item);
	        history.setPrice(item.getPrice());
	        history.setQuantity(invoiceItem.getQuantity());
	        

        historyRepository.save(history);
	}
	
	/**
	 * Creates a history record for editing an item in an invoice, logging the invoice, item details, and the user who own the invoice
	 * 
	 * @param invoice The invoice in which the item was edited.
	 * @param invoiceItem The item whose details were edited.
	 * @param user The user who edited the item in the invoice.
	 */
	public void CreateEditItemHistoryRecoard(Invoice invoice, InvoiceItem invoiceItem, User user) {
	    
		 Item item = invoiceItem.getItem();
		 
         History inactive = historyRepository.findByInvoiceIdAndStatusAndItemId(invoice.getId(),1, item.getId());
       inactive.setStatus(0);
       historyRepository.save(inactive);
       
		 History history = new History();
	        history.setInvoice(invoice);
	        history.setUser(user);
	        history.setAction("Edited");
	        history.setDescription("Edit Quantity");
	        history.setType("Item");
	        history.setStatus(1);
	        history.setItem(item);
	        history.setPrice(item.getPrice());
	        history.setQuantity(invoiceItem.getQuantity());
	        
       historyRepository.save(history);
	}
	
}


