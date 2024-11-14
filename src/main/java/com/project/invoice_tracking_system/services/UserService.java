package com.project.invoice_tracking_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.invoice_tracking_system.dots.UserRole;
import com.project.invoice_tracking_system.entities.Role;
import com.project.invoice_tracking_system.entities.User;
import com.project.invoice_tracking_system.repositories.RoleRepository;
import com.project.invoice_tracking_system.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	
	public User changeRole(UserRole userRole) throws Exception {
		String email = userRole.getEmail();
		int roleId = userRole.getRoleId();
		User user = userRepository.findByEmail(email).get();
		Role role = roleRepository.findById(roleId).get();
		if(role != null) {
		user.setRole(role);
		return userRepository.save(user);
		}else {
			throw new Exception();
		}
	}
}
