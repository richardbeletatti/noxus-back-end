package com.alemcrm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenUtil {

	   @Value("${jwt.secret}")
	    private String jwtSecret;

	    private Key SECRET_KEY;
	    private static final long EXPIRATION_TIME = 86400000;

	    @PostConstruct
	    public void init() {
	        this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
	    }
	    
    public String generateToken(Long id, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("id", id) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public Long getUserIdFromToken(String token) {
        return decodeToken(token).get("id", Long.class);
    }


    public String getEmailFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return decodeToken(token).get("role", String.class);
    }
}