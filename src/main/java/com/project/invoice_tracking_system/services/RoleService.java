package com.project.invoice_tracking_system.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.invoice_tracking_system.entities.Role;
import com.project.invoice_tracking_system.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired	
	private RoleRepository roleRepository;
	   public List<Role> getAllRoles(){
	
			List<Role> roles = new ArrayList<>();
			roleRepository.findAll()
			.forEach(roles:: add);
			return roles;	
	   }
	
}
