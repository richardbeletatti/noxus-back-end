package com.alemcrm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alemcrm.model.User;
import com.alemcrm.service.UserService;
import com.alemcrm.util.TokenUtil;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        System.out.println("Tentativa de login para email: " + email);

        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Usuário encontrado: " + user.getEmail());

            if (user.getPassword().equals(password)) {
                System.out.println("Senha correta!");

                // Gerar o JWT real
                String token = TokenUtil.generateToken(user.getEmail(), user.getRole());

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", user.getRole());
                response.put("name", user.getName());

                return ResponseEntity.ok(response);
            } else {
                System.out.println("Senha incorreta!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
            }
        } else {
            System.out.println("Usuário não encontrado!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
        }
    }
}