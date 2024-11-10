package com.project.invoice_tracking_system.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.project.invoice_tracking_system.Exception_handling.exception.ItemAlreadyExistsException;
import com.project.invoice_tracking_system.dots.InvoiceItemDto;
import com.project.invoice_tracking_system.entities.History;
import com.project.invoice_tracking_system.entities.Invoice;
import com.project.invoice_tracking_system.entities.InvoiceItem;
import com.project.invoice_tracking_system.entities.Item;
import com.project.invoice_tracking_system.entities.User;
import com.project.invoice_tracking_system.repositories.HistoryRepository;
import com.project.invoice_tracking_system.repositories.InvoiceItemRepository;
import com.project.invoice_tracking_system.repositories.InvoiceRepository;
import com.project.invoice_tracking_system.repositories.ItemRepository;
import com.project.invoice_tracking_system.repositories.UserRepository;


/**
 * InvoiceService is responsible for managing the business logic related to invoices.
 * It handles tasks such as creating new invoices, retrieving invoices, calculating total prices,
 * and recording history for actions performed on invoices.
 * This service interacts with various repositories to access and manage data related to invoices, items, users,
 * invoice items, and historical actions.
 *
 * The InvoiceService utilizes the following repositories:
 * - InvoiceRepository: For CRUD operations on invoices.
 * - ItemRepository: For managing items available for invoices.
 * - UserRepository: For user-related operations, including retrieving the user associated with an invoice.
 * - InvoiceItemRepository: For managing the relationship between invoices and items.
 * - HistoryRepository: For recording and retrieving history related to invoice actions.
 * 
 * @see InvoiceRepository
 * @see ItemRepository
 * @see UserRepository
 * @see InvoiceItemRepository
 * @see HistoryRepository
 */
@Service
public class InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
    private ItemRepository itemRepository;
	@Autowired
    private UserRepository userRepository;
	@Autowired
	private InvoiceItemRepository invoiceItemRepository;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private HistoryRepository historyRepository;
	
	/**
	 * Retrieves a paginated list of all invoices, with support for sorting.
	 * 
	 * @param pageNumber The page number to retrieve (starting from 0).
	 * @param pageSize The number of invoices per page.
	 * @param sortBy The field to sort the results by (e.g., "date", "totalPrice").
	 * @return A paginated list of all invoices.
	 */
	public Page<Invoice> getAllInvoices(int pageNumber, int pageSize, String sortBy){
    	Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return  invoiceRepository.findAll(pageable);
	}
	
	/**
	 * Retrieves a paginated list of all invoices associated with a specific user, identified by their email,
	 * with support for sorting.
	 * 
	 * @param email The email address of the user to filter invoices by.
	 * @param pageNumber The page number to retrieve (starting from 0).
	 * @param pageSize The number of invoices per page.
	 * @param sortBy The field to sort the results by (e.g., "date", "totalPrice").
	 * @return A paginated list of invoices for the specified user.
	 */
	public Page<Invoice> getAllUserInvoices(String email, int pageNumber, int pageSize, String sortBy){
		User user = userRepository.findByEmail(email).get();
    	Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return  invoiceRepository.findAllByUserId(user.getId(), pageable);
	}
	
	/**
	 * Retrieves the details of a specific invoice by its ID, associated with the given email address.
	 * 
	 * @param id The ID of the invoice to retrieve.
	 * @param email The email address of the user requesting the invoice details.
	 * @return The Invoice object containing details of the requested invoice.
	 * @throws InvoiceNotFoundException If no invoice with the provided ID is found.
	 */
	public Invoice getInvoice(int id, String email){
		User user = userRepository.findByEmail(email).get();
		Invoice invoice = invoiceRepository.findById(id).get();
		if( (user.getRole().getName().equalsIgnoreCase("SUPERUSER")) || (user.getRole().getName().equalsIgnoreCase("AUDITOR")) || (invoice.getUser().getId() == user.getId()) ) {
			return invoiceRepository.findById(id).get();
		}
        throw new AccessDeniedException("Access Denied");
	}
	
	/**
	 * Creates a new invoice with the specified items for a user identified by their email.
	 * 
	 * @param email The email address of the user creating the invoice.
	 * @param invoiceItemsDto A list of InvoiceItemDto objects containing details of the items to be added to the invoice.
	 * @return The created Invoice object.
	 * @throws InvalidInvoiceDataException If the invoice data is invalid or incomplete.
	 */
	public Invoice createInvoice(String email, List<InvoiceItemDto> invoiceItemsDto) {
        Invoice invoice = new Invoice();
		User user = userRepository.findByEmail(email).get();
        invoice.setUser(user);
        
        invoiceRepository.save(invoice);
        // make a record in history.
        historyService.CreateInvoiceHistoryRecoard(invoice, user);

        for (InvoiceItemDto invoiceItemDto : invoiceItemsDto) {
        	
        	invoice = addNewItem(invoice.getId(), email, invoiceItemDto);
        }
    
        return invoiceRepository.findById(invoice.getId()).get();   
    }
	
	/**
	 * Adds a new item to an existing invoice. This updates the invoice with the provided item details.
	 * 
	 * @param id The ID of the invoice to update.
	 * @param email The email address of the user adding the item to the invoice.
	 * @param invoiceItemDto The InvoiceItemDto containing the details of the new item to add.
	 * @return The updated Invoice object with the newly added item.
	 * @throws InvoiceNotFoundException If no invoice with the provided ID is found.
	 */
	public Invoice addNewItem(int id, String email, InvoiceItemDto invoiceItemDto){
		User user = userRepository.findByEmail(email).get();
		Invoice invoice = invoiceRepository.findById(id).get();
		if( ((user.getRole().getName().equalsIgnoreCase("SUPERUSER")) || (invoice.getUser().getId() == user.getId()) ) && invoice.isDeleted() == false) {

		    Item item = itemRepository.findById(invoiceItemDto.getItemId())
                  .orElseThrow(() -> new RuntimeException("Item not found"));
		    
		    // check if the item already exist in invoice.
		    Optional <InvoiceItem> itemExist =  invoiceItemRepository.findByInvoiceIdAndItemId(invoice.getId(), item.getId());
		    if(itemExist.isPresent()) {
	           throw new ItemAlreadyExistsException(item.getItemName());

		    }
		    	
			InvoiceItem invoiceItem = new InvoiceItem();
		    invoiceItem.setItem(item);
		    invoiceItem.setQuantity(invoiceItemDto.getQuantity());
			invoice.addItem(invoiceItem);
	        invoiceRepository.save(invoice);
            // make a record in history.
	        historyService.CreateAddItemHistoryRecoard(invoice, invoiceItemDto, user);
	        return invoice;

		}
        throw new AccessDeniedException("Access Denied");
	
	}
	
	/**
	 * Deletes an item from an existing invoice.
	 * 
	 * @param id The ID of the invoice from which to delete the item.
	 * @param email The email address of the user deleting the item.
	 * @return The updated Invoice object after the item is deleted.
	 * @throws InvoiceNotFoundException If no invoice with the provided ID is found.
	 */
	public Invoice deleteItem(int id, String email){
		User user = userRepository.findByEmail(email).get();
		InvoiceItem invoiceItem = invoiceItemRepository.findById(id).get();
		Invoice invoice = invoiceItem.returnInvoice();
		
		if( ((user.getRole().getName().equalsIgnoreCase("SUPERUSER")) || (invoice.getUser().getId() == user.getId()) )  && invoice.isDeleted() == false) {
			invoiceItemRepository.deleteById(id);
			// make a record to history.
	        historyService.CreateDeleteItemHistoryRecoard(invoice, invoiceItem, user);
			return invoiceRepository.findById(invoice.getId()).get();

		}	
        throw new AccessDeniedException("Access Denied");

	}
	
	/**
	 * Edits the quantity of an item in an invoice.
	 * 
	 * @param id The ID of the invoice containing the item to be edited.
	 * @param email The email address of the user editing the item.
	 * @param newQuantity The new quantity to set for the item.
	 * @return The updated Invoice object after the quantity has been changed.
	 * @throws InvoiceNotFoundException If no invoice with the provided ID is found.
	 * @throws ItemNotFoundException If the item is not found in the invoice.
	 */
	public Invoice editQuantity(int id, String email, int newQuantity){
		User user = userRepository.findByEmail(email).get();
		InvoiceItem invoiceItem = invoiceItemRepository.findById(id).get();
		Invoice invoice = invoiceItem.returnInvoice();
		
		if( ((user.getRole().getName().equalsIgnoreCase("SUPERUSER")) || (invoice.getUser().getId() == user.getId()) ) && invoice.isDeleted() == false) {
			invoiceItem.setQuantity(newQuantity);
		    invoiceItemRepository.save(invoiceItem);
		    // make a record to history.
	        historyService.CreateEditItemHistoryRecoard(invoice, invoiceItem, user);
			return invoiceRepository.findById(invoice.getId()).get();

		}	
        throw new AccessDeniedException("Access Denied");

	}

	/**
	 * Deletes an entire invoice identified by its ID.
	 * 
	 * @param id The ID of the invoice to delete.
	 * @param email The email address of the user requesting the deletion of the invoice.
	 * @throws InvoiceNotFoundException If no invoice with the provided ID is found.
	 */
	public void deleteInvoice(int id, String email){
		User user = userRepository.findByEmail(email).get();
		Invoice invoice = invoiceRepository.findById(id).get();
		if( ((user.getRole().getName().equalsIgnoreCase("SUPERUSER")) || (invoice.getUser().getId() == user.getId()) ) && invoice.isDeleted() == false) {
		    
			// i should make delete first then save to history..
			invoice.setDeleted(true);
			invoiceRepository.save(invoice);
			// make a record in history.
	        historyService.CreateDeleteInvoiceHistoryRecoard(invoice, user);
		
		}else {
	        throw new AccessDeniedException("Access Denied");

		}
	}
	
	/**
	 * Retrieves the history of actions performed on a specific invoice.
	 * 
	 * @param id The ID of the invoice to retrieve the history for.
	 * @param email The email address of the user requesting the history.
	 * @return A list of History objects representing the actions performed on the invoice.
	 */
	public List<History> getInvoiceHistory(int id, String email){
		User user = userRepository.findByEmail(email).get();
		Invoice invoice = invoiceRepository.findById(id).get();
		if( (user.getRole().getName().equalsIgnoreCase("SUPERUSER")) || (user.getRole().getName().equalsIgnoreCase("AUDITOR")) || (invoice.getUser().getId() == user.getId()) ) {
			return historyRepository.findAllByInvoiceId(id);
		}
        throw new AccessDeniedException("Access Denied");
	}
	
	/**
	 * Searches for invoices using various filters, such as username, invoice ID, and user email,
	 * with pagination and sorting support.
	 * 
	 * @param username The username of the user to filter invoices by (optional).
	 * @param invoiceId The ID of the invoice to search for (optional).
	 * @param email The email address of the user requesting the search.
	 * @param pageNumber The page number to retrieve (starting from 0).
	 * @param pageSize The number of invoices per page.
	 * @param sortBy The field to sort the results by (e.g., "date", "totalPrice").
	 * @return A paginated list of invoices that match the search criteria.
	 */
	 public Page<Invoice> searchInvoices(String username, Long invoiceId, String email, int pageNumber, int pageSize, String sortBy) {
	    	Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
	        return invoiceRepository.searchByUsernameOrInvoiceId(username, invoiceId, pageable);
	 }
	 
	 /**
	  * Calculates the total price for a specific invoice, including the cost of all items in the invoice.
	  * 
	  * @param invoiceId The ID of the invoice for which to calculate the total price.
	  * @return The total price of the invoice.
	  * @throws InvoiceNotFoundException If no invoice with the provided ID is found.
	  * @throws CalculationException If there is an error during the price calculation.
	  */
     public Double calculateTotalPrice(int invoiceId) throws Exception {
	        Invoice invoice = invoiceRepository.findById(invoiceId).get();
	        if(invoice == null) {
	           throw new Exception("error");
		    }
	        return invoiceItemRepository.calculateTotalPriceByInvoiceId(invoiceId);
    }
}
