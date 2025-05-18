package com.alemcrm.util;


import java.util.Base64;

public class TokenUtil {

    public static String generateFakeToken(String email, String role) {
        String tokenPayload = email + ":" + role;
        return Base64.getEncoder().encodeToString(tokenPayload.getBytes());
    }

    public static String decodeFakeToken(String token) {
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        return new String(decodedBytes);
    }
}


