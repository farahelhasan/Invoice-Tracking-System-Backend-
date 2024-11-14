package com.project.invoice_tracking_system.Exception_handling.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.invoice_tracking_system.Exception_handling.exception.EmailAlreadyExistsException;
import com.project.invoice_tracking_system.Exception_handling.exception.ItemAlreadyExistsException;
import com.project.invoice_tracking_system.response.ErrorResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(EmailAlreadyExistsException.class)
	    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {	        
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
	        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	    }
	  
	 @ExceptionHandler(ItemAlreadyExistsException.class)
	    public ResponseEntity<ErrorResponse> handleItemAlreadyExists(ItemAlreadyExistsException ex) {	        
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
	        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	    }
	 
	  @ExceptionHandler(AccessDeniedException.class)
	    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());
	        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	   }
	  
	  @ResponseStatus(HttpStatus.UNAUTHORIZED)
	  @ExceptionHandler(AuthenticationException.class)
	    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
	        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	   }
	  
	  
	   @ExceptionHandler(SecurityException.class)
	    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException ex) {
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	   
	   
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
}