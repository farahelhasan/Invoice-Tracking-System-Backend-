package com.project.invoice_tracking_system.controllers;


import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.invoice_tracking_system.dots.InvoiceDetailstDto;
import com.project.invoice_tracking_system.dots.InvoiceItemDto;
import com.project.invoice_tracking_system.services.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * Controller for managing invoice-related operations.
 * Provides endpoints to fetch, create, update, and delete invoices,
 * along with additional functionality for invoice item management and history tracking.
 * <p>
 * Secured endpoints are protected by roles such as SUPERUSER, USER, and AUDITOR.
 */
@Tag(name = "Invoice Management", description = "APIs for managing invoices")
@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService; 
    Logger logger = LoggerFactory.getLogger(ApplicationHome.class);
    
    /**
     * Fetches a list of all invoices with pagination and sorting options.
     * <p>
     * Accessible by roles: SUPERUSER, AUDITOR
     * 
     * @param page   The page number (default: 0)
     * @param size   The number of invoices per page (default: 10)
     * @param sortBy The field to sort by (default: "id")
     * @return A ResponseEntity containing a list of invoices.
     */
    @Operation(summary = "Get all invoices", description = "Fetches all invoices with pagination and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of invoices retrieved successfully")
    })
	@Secured({"ROLE_SUPERUSER","ROLE_AUDITOR"})
    @GetMapping("")
    public ResponseEntity<?> getAllInvoices(
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
		
        logger.info("get all invoices controller");
        return ResponseEntity.ok(invoiceService.getAllInvoices(page, size, sortBy));
    }
	
    /**
     * Fetches a list of invoices for a specific user.
     * <p>
     * Accessible by roles: SUPERUSER, USER, AUDITOR
     * 
     * @param principal The authenticated user (obtained from the Principal object)
     * @param page      The page number (default: 0)
     * @param size      The number of invoices per page (default: 10)
     * @param sortBy    The field to sort by (default: "id")
     * @return A ResponseEntity containing a list of invoices for the authenticated user.
     */
    @Operation(summary = "Get invoices for a specific user", description = "Fetches invoices for the authenticated user with pagination and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of user-specific invoices retrieved successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER","ROLE_AUDITOR"})
    @GetMapping("/user")
    public ResponseEntity<?> getAllUserInvoices(Principal principal,
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy){

        logger.info("get all invoices for specific user controller");
		String email = principal.getName();
        return ResponseEntity.ok(invoiceService.getAllUserInvoices(email, page, size, sortBy));
    }
	
    /**
     * Fetches details of a specific invoice by ID.
     * <p>
     * Accessible by roles: SUPERUSER, USER, AUDITOR
     * 
     * @param id The ID of the invoice to fetch
     * @param principal The authenticated user (obtained from the Principal object)
     * @return A ResponseEntity containing the invoice details.
     */ 
    @Operation(summary = "Get a specific invoice", description = "Fetches details of a specific invoice by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER", "ROLE_AUDITOR"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable int id, Principal principal ) {
		
        logger.info("get invoice controller");
		String email = principal.getName();
        return ResponseEntity.ok(invoiceService.getInvoice(id, email));
    }
	
    /**
     * Creates a new invoice for the authenticated user.
     * <p>
     * Accessible by roles: SUPERUSER, USER
     * 
     * @param invoiceDetailstDto The invoice details to create
     * @param principal The authenticated user (obtained from the Principal object)
     * @return A ResponseEntity containing the newly created invoice.
     */
    @Operation(summary = "Create a new invoice", description = "Creates a new invoice for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice created successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER"})
    @PostMapping("")
    public ResponseEntity<?> createNewInvoice(@RequestBody InvoiceDetailstDto invoiceDetailstDto, Principal principal) {
		
        logger.info("create new invoice controller");
		String email = principal.getName();
        return ResponseEntity.ok(invoiceService.createInvoice(email, invoiceDetailstDto.getInvoiceItemsDto()));
    }
	
    /**
     * Creates a new invoice for any user by an admin.
     * <p>
     * Accessible only by the SUPERUSER role.
     * 
     * @param invoiceDetailstDto The invoice details to create
     * @return A ResponseEntity containing the newly created invoice.
     */
    @Operation(summary = "Create an invoice for any user", description = "Creates a new invoice on behalf of another user by an admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice created successfully")
    })
	@Secured("ROLE_SUPERUSER")
    @PostMapping("/superuser")
    public ResponseEntity<?> createNewInvoiceByAdmain(@RequestBody InvoiceDetailstDto invoiceDetailstDto) {
		
        logger.info("create new invoice by admin controller");
		String email =  invoiceDetailstDto.getEmail();
        return ResponseEntity.ok(invoiceService.createInvoice(email, invoiceDetailstDto.getInvoiceItemsDto()));
    }
	
    /**
     * Adds a new item to an existing invoice.
     * <p>
     * Accessible by roles: SUPERUSER, USER
     * 
     * @param invoiceItem The item to add
     * @param id The ID of the invoice
     * @param principal The authenticated user (obtained from the Principal object)
     * @return A ResponseEntity indicating the success of the operation.
     */
    @Operation(summary = "Add a new item to an invoice", description = "Adds a new item to an existing invoice by invoice ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item added successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER"})
    @PostMapping("/{id}/items")
    public ResponseEntity<?> addNewIteme(@RequestBody InvoiceItemDto invoiceItem, @PathVariable int id, Principal principal) {
		
        logger.info("add new item controller");
		String email = principal.getName();
		return ResponseEntity.ok(invoiceService.addNewItem(id, email, invoiceItem));
    }
	
    /**
     * Deletes a specific item from an invoice by item ID.
     * <p>
     * Accessible by roles: SUPERUSER, USER
     * 
     * @param id The ID of the item to delete
     * @param principal The authenticated user (obtained from the Principal object)
     * @return A ResponseEntity indicating the success of the operation.
     */
    @Operation(summary = "Delete an item from an invoice", description = "Deletes a specific item from an invoice by item ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item deleted successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER"})
    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteIteme(@PathVariable int id, Principal principal) {
		
        logger.info("delete item controller");
		String email = principal.getName();
		return ResponseEntity.ok(invoiceService.deleteItem(id, email));
    }
	
    /**
     * Edits the quantity of a specific item in an invoice.
     * <p>
     * Accessible by roles: SUPERUSER, USER
     * 
     * @param invoiceItem The item with the updated quantity
     * @param principal The authenticated user (obtained from the Principal object)
     * @return A ResponseEntity indicating the success of the operation.
     */
    @Operation(summary = "Edit quantity of an item in an invoice", description = "Updates the quantity of a specific item in an invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quantity updated successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER"})
    @PatchMapping("/items")
    public ResponseEntity<?> editQuantity(@RequestBody InvoiceItemDto invoiceItem, Principal principal) {
		
        logger.info("edit item quantity controller");
		String email = principal.getName();
		int invoiceItemId = invoiceItem.getItemId();
		int quantity = invoiceItem.getQuantity();
		return ResponseEntity.ok(invoiceService.editQuantity(invoiceItemId, email, quantity));
    }
	
    /**
     * Deletes a specific invoice by its ID.
     * <p>
     * Accessible by roles: USER, SUPERUSER
     * 
     * @param id The ID of the invoice to delete
     * @param principal The authenticated user (obtained from the Principal object)
     * @return A ResponseEntity indicating the success of the operation.
     */
    @Operation(summary = "Delete an invoice", description = "Deletes a specific invoice by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice deleted successfully")
    })
	@Secured({"ROLE_USER", "ROLE_SUPERUSER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable int id ,Principal principal ) {
		
        logger.info("delete invoice controller");
		String email = principal.getName();
		invoiceService.deleteInvoice(id, email);
        return ResponseEntity.ok("delete invoice succesfully");
    }
	
    /**
     * Tracks the history of a specific invoice, including changes to its status and items.
     * <p>
     * Accessible by roles: SUPERUSER, AUDITOR
     * 
     * @param id The ID of the invoice
     * @param principal The authenticated user (obtained from the Principal object)
     * @return A ResponseEntity containing the history of the invoice.
     */
    @Operation(summary = "View invoice history", description = "Fetches the history of a specific invoice by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice history retrieved successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER", "ROLE_AUDITOR"})
    @GetMapping("/{id}/history")
    public ResponseEntity<?> getInvoiceHistory(@PathVariable int id, Principal principal ) {
		
        logger.info("get invoice history controller");
		String email = principal.getName();
        return ResponseEntity.ok(invoiceService.getInvoiceHistory(id, email));
    }
	
    /**
     * Searches invoices based on the provided invoice ID or username.
     * <p>
     * This endpoint allows searching for invoices by either the invoice ID or the username of the user. 
     * It supports pagination and sorting options.
     * The results are filtered based on the authenticated user's email.
     * 
     * @param principal The authenticated user’s principal containing their email.
     * @param username Optional parameter to filter by username.
     * @param invoiceId Optional parameter to filter by invoice ID.
     * @param page The page number for pagination (default is 0).
     * @param size The number of records per page (default is 10).
     * @param sortBy The field by which the results should be sorted (default is "id").
     * @return A ResponseEntity containing the search results as a list of invoices.
     * @throws Exception If an error occurs during the search operation.
     */
    @Operation(summary = "Search invoices", description = "Searches invoices by invoice ID or username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER", "ROLE_AUDITOR"})
	@GetMapping("/search")
    public ResponseEntity<?> searchInvoices(
    	Principal principal,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) Long invoiceId, 
		@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy) {
        
        logger.info("search invoice controller");
		String email = principal.getName();
        return ResponseEntity.ok(invoiceService.searchInvoices(username, invoiceId, email, page, size, sortBy));
    }	
	
    /**
     * Calculates the total price for a specific invoice by invoice ID.
     * <p>
     * This endpoint calculates the total price for the invoice identified by its ID. 
     * The calculation takes into account all the items associated with the invoice.
     * 
     * @param id The ID of the invoice for which the total price should be calculated.
     * @return The total price of the specified invoice.
     * @throws Exception If an error occurs during the total price calculation.
     */
    @Operation(summary = "Calculate total invoice price", description = "Calculates the total price of a specific invoice by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total price calculated successfully")
    })
	@Secured({"ROLE_SUPERUSER", "ROLE_USER", "ROLE_AUDITOR"})
	@GetMapping("/{id}/calculateTotal")
    public Double calculateTotalPrice(@PathVariable int id) throws Exception {
		
        logger.info("calculate total price controller");
        return invoiceService.calculateTotalPrice(id);
    }
	
	
}


