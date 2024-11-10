package com.project.invoice_tracking_system.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.invoice_tracking_system.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Principal;

/**
 * ItemController is responsible for handling requests related to managing items
 * within the invoice tracking system.
 * <p>
 * This controller provides endpoints for retrieving all items, as well as retrieving 
 * items that have not been added to a specific invoice.
 * The controller also enforces access control based on user roles (SUPERUSER, USER).
 */
@Tag(name = "Item Management", description = "APIs for managing items")
@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;
    Logger logger = LoggerFactory.getLogger(ApplicationHome.class);

    /**
     * Retrieves a list of all items that can be added to an invoice.
     * <p>
     * This endpoint fetches all the items available in the system for inclusion in a new invoice. 
     * Access is restricted to users with the role SUPERUSER or USER.
     *
     * @return A ResponseEntity containing a list of items available in the system.
     */
    @Operation(summary = "Get all items", description = "Retrieves a list of all items that can be added to an invoice.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of items retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - User lacks the required role")
    })
    @Secured({"ROLE_SUPERUSER","ROLE_USER"})
    @GetMapping("")
    public ResponseEntity<?> getAllItems() {
    	
        logger.info("get all items controller");
        return ResponseEntity.ok(itemService.getAllItems());
    }

    /**
     * Retrieves items that have not been added to a specified invoice.
     * <p>
     * This endpoint allows users to fetch items that are not yet associated with a given invoice, 
     * when the user edit exist invoice to add new item to it.
     * identified by its invoiceId. Only users with the role SUPERUSER or USER can access this 
     * endpoint, and the invoice must exist for the query to be successful.
     *
     * @param invoiceId The ID of the invoice for which items are being queried.
     * @param principal The authenticated user, whose email will be used for filtering results.
     * @return A ResponseEntity containing the list of items not in the specified invoice.
     * @throws Exception If the invoice is not found or an error occurs.
     */
    @Operation(summary = "Get items not in an invoice", description = "Retrieves items that have not yet been added to a specified invoice.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of items not in the specified invoice retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - User lacks the required role"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @Secured({"ROLE_SUPERUSER","ROLE_USER"})
    @GetMapping("/{invoiceId}")
    public ResponseEntity<?> getItemsNotInInvoice(@PathVariable int invoiceId, Principal principal) {
    	
        logger.info("get all items not in specific invoice controller");
        String email = principal.getName();
        return ResponseEntity.ok(itemService.findItemsNotInInvoice(invoiceId, email));
    }
}
