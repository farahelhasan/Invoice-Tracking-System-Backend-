package com.project.invoice_tracking_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.invoice_tracking_system.dots.UserRole;
import com.project.invoice_tracking_system.services.InvoiceService;
import com.project.invoice_tracking_system.services.RoleService;
import com.project.invoice_tracking_system.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	 @Autowired
	  private UserService userService;
	 @Autowired
	  private RoleService roleService;
	 
	 
	// change role
	@Secured("ROLE_SUPERUSER")
    @PatchMapping("/roles")
    public ResponseEntity<?> changeRole(@RequestBody UserRole userRole) throws Exception {
		return ResponseEntity.ok(userService.changeRole(userRole));
    }
	
	// get all role
	@Secured("ROLE_SUPERUSER")
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles()  {
		return ResponseEntity.ok(roleService.getAllRoles());
    }
}