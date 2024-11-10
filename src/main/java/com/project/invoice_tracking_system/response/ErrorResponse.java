package com.project.invoice_tracking_system.response;

public class ErrorResponse {
    private int statusCode;
    private String message;

    public ErrorResponse(String message, int statusCode) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}