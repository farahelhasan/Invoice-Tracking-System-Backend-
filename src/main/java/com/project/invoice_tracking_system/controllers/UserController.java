package com.project.invoice_tracking_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
	
 //   @PreAuthorize("hasRole('ROLE_ADMIN')")
//	@Secured("ROLE_SUPERUSER")
//    @PostMapping("/admin-only")
//    public ResponseEntity<?> adminOnlyEndpoint() {
//    	System.out.println("Hello, this is a console message.");
//
//        // Admin-only logic here
//        return ResponseEntity.ok("This is an admin-only endpoint.");
//    }
//   // @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//	@Secured({"ROLE_USER", "ROLE_SUPERUSER"})
//    @GetMapping("/user-or-admin")
//    public ResponseEntity<?> userOrAdminEndpoint() {
//        // Logic for users or admins
//        return ResponseEntity.ok("This is accessible to users and admins.");
//    }
//	
//	@Secured("ROLE_USER")
//    @GetMapping("/user-only")
//    public ResponseEntity<?> userEndpoint() {
//        // Logic for users 
//        return ResponseEntity.ok("This is accessible to users only.");
//    }
}
