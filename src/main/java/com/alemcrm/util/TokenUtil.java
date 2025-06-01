package com.alemcrm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class TokenUtil {

    private static final String SECRET = "gD93!kLm#2pZr7TnW@8xUeQv$4HsJcNb";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION_TIME = 86400000; // One day in milliseconds

    public static String generateToken(Long id, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("id", id) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getEmailFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    public static String getRoleFromToken(String token) {
        return decodeToken(token).get("role", String.class);
    }
}