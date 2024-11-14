package com.project.invoice_tracking_system.services;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.authentication.AuthenticationManager;

import com.project.invoice_tracking_system.Exception_handling.exception.EmailAlreadyExistsException;
import com.project.invoice_tracking_system.Exception_handling.exception.UnauthorizedException;
import com.project.invoice_tracking_system.dots.LoginUserDto;
import com.project.invoice_tracking_system.dots.RegisterUserDto;
import com.project.invoice_tracking_system.entities.Role;
import com.project.invoice_tracking_system.entities.User;
import com.project.invoice_tracking_system.repositories.UserRepository;


/**
 * AuthenticationService is responsible for handling user authentication and registration.
 * It provides methods to sign up new users, authenticate existing users, and validate login credentials.
 * 
 * This service interacts with the UserRepository for storing and retrieving user data, uses
 * a PasswordEncoder for encoding and verifying passwords, and utilizes the AuthenticationManager
 * for authenticating user credentials against the system.
 * 
 * The AuthenticationService is responsible for:
 * - Registering new users with encrypted passwords.
 * - Authenticating users based on their credentials.
 * - Managing authentication tokens for logged-in users.
 * 
 * @see UserRepository
 * @see PasswordEncoder
 * @see AuthenticationManager
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    Logger logger = LoggerFactory.getLogger(ApplicationHome.class);

    /**
     * Constructs a new instance of AuthenticationService.
     * 
     * @param userRepository The repository for performing operations on user data.
     * @param authenticationManager The manager responsible for authenticating user credentials.
     * @param passwordEncoder The encoder used for password encryption and verification.
     */
    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     * 
     * This method performs validation on the provided user details, encodes the user's password,
     * and then saves the user information to the database.
     * 
     * @param registerUserDto The user data to be used for registration.
     * @return The registered user entity with the encrypted password.
     * @throws EmailAlreadyExistsException if the provided user data is already exist.
     */
    public User signup(RegisterUserDto input) {
    	Optional<User> userOptional = userRepository.findByEmail(input.getEmail());

    	if(userOptional.isPresent()) {
            throw new EmailAlreadyExistsException(input.getEmail());
        }
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(new Role(3,"USER"));
        return userRepository.save(user);
    }

    /**
     * Authenticates a user based on the provided credentials.
     * 
     * This method checks if the user exists in the database and if the provided password
     * matches the stored (encoded) password.
     * 
     * @param loginUserDto The login credentials of the user.
     * @return The authenticated user entity.
     * @throws Exception if the login credentials are invalid.
     */

    public User authenticate(LoginUserDto input) {
        // Attempt authentication with the provided credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
            )
        );

        // Retrieve the user from the database if authentication succeeds
        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

//    public User authenticate(LoginUserDto input) {
//    	// check if valid
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        input.getEmail(),
//                        input.getPassword()
//                )
//        );
//
//        // check in database 
//        return userRepository.findByEmail(input.getEmail())
//                .orElseThrow();
//    }
}