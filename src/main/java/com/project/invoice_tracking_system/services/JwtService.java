package com.project.invoice_tracking_system.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.invoice_tracking_system.entities.User;

/**
 * The JwtService class provides functionality for working with JSON Web Tokens (JWT).
 * It handles token creation, extraction, validation, and expiration checks. 
 * The service is primarily used for handling user authentication and role-based access control 
 * within the application.
 * 
 * The JwtService utilizes the following configuration properties:
 * - secretKey: A secret key used for signing JWTs.
 * - jwtExpiration: The expiration time of the JWT token.
 * 
 * @see User
 */
@Service
public class JwtService {
	 /**
     * Secret key used to sign the JWT tokens.
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * The expiration time of the JWT token in milliseconds.
     */
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extracts the username from the provided JWT token.
     * 
     * @param token The JWT token from which the username is extracted.
     * @return The username (subject) of the JWT token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extracts the role from the provided JWT token.
     * 
     * @param token The JWT token from which the role is extracted.
     * @return The role of the user encoded in the JWT token.
     */
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class); // Retrieves the single role
    }

    /**
     * Extracts a specific claim from the JWT token using a provided function.
     * 
     * @param token The JWT token from which the claim is extracted.
     * @param claimsResolver A function to extract the claim from the Claims object.
     * @param <T> The type of the claim value.
     * @return The value of the claim extracted from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user details.
     * 
     * @param userDetails The user details for whom the JWT is generated.
     * @return A JWT token for the given user.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the given user details with additional claims.
     * 
     * @param extraClaims A map of additional claims to be added to the token.
     * @param userDetails The user details for whom the JWT is generated.
     * @return A JWT token with the provided claims and user details.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Add the role claim here
        extraClaims.put("role", ((User) userDetails).getRole().getName());
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Retrieves the expiration time of the JWT token.
     * 
     * @return The expiration time in milliseconds.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Builds a JWT token with the provided claims, user details, and expiration time.
     * 
     * @param extraClaims A map of additional claims to be added to the token.
     * @param userDetails The user details for whom the JWT is generated.
     * @param expiration The expiration time of the token in milliseconds.
     * @return A JWT token built with the specified claims, user details, and expiration time.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if the provided JWT token is valid for the given user details.
     * 
     * @param token The JWT token to be validated.
     * @param userDetails The user details to compare with the token.
     * @return True if the token is valid for the user, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the provided JWT token has expired.
     * 
     * @param token The JWT token to check for expiration.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date of the JWT token.
     * 
     * @param token The JWT token from which the expiration date is extracted.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the provided JWT token.
     * 
     * @param token The JWT token from which claims are extracted.
     * @return A Claims object containing all the claims in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key for the JWT token from the secret key.
     * 
     * @return The signing key for the JWT token.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}