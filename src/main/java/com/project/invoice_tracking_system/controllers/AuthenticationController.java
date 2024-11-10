package com.project.invoice_tracking_system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.invoice_tracking_system.dots.LoginUserDto;
import com.project.invoice_tracking_system.dots.RegisterUserDto;
import com.project.invoice_tracking_system.entities.User;
import com.project.invoice_tracking_system.response.LoginResponse;
import com.project.invoice_tracking_system.services.AuthenticationService;
import com.project.invoice_tracking_system.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controller responsible for user authentication actions such as login and registration.
 * This class handles user signup and login requests and integrates with the authentication
 * and JWT services to manage user sessions.
 */
@Tag(name = "Authentication Management", description = "APIs for managing authentication")
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(ApplicationHome.class);

    
	/**
	 * Constructs an AuthenticationController with the provided JwtService and AuthenticationService.
	 * 
	 * @param jwtService The service used to generate and validate JWT tokens.
	 * @param authenticationService The service responsible for handling user authentication.
	 */
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Registers a new user in the system.
     * This endpoint receives the user registration details, processes them,
     * and returns the registered user object if successful.
     *
     * @param registerUserDto The DTO containing the user registration details.
     * @return ResponseEntity containing the registered user object.
     */
    @Operation(summary = "User registration", description = "Registers a new user in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request - User data is incomplete or incorrect")
    })
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    	
        logger.info("signup controller");
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Authenticates a user by validating their login credentials and returns a JWT token.
     * This endpoint validates the user's credentials, generates a JWT token upon successful authentication,
     * and returns the token and its expiration time.
     *
     * @param loginUserDto The DTO containing the user's login credentials (username and password).
     * @return ResponseEntity containing the JWT token and its expiration time.
     */
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully, token returned"),
        @ApiResponse(responseCode = "401", description = "Authentication failed - Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
    	
        logger.info("login controller");
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
