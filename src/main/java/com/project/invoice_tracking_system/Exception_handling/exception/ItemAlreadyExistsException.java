package com.project.invoice_tracking_system.Exception_handling.exception;

public class ItemAlreadyExistsException extends RuntimeException {
    public ItemAlreadyExistsException(String itemName) {
        super("Item already exists: " + itemName);
    }

}
