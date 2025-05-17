package com.alemcrm.controller;

import com.alemcrm.model.User;
import com.alemcrm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") 
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getPassword().equals(password)) {
                // Login bem-sucedido
                Map<String, String> response = new HashMap<>();
                response.put("token", "fake-jwt-token"); // você pode trocar isso depois por JWT real
                response.put("role", user.getRole());
                response.put("name", user.getName());

                return ResponseEntity.ok(response);
            }
        }

        // Falha no login
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
