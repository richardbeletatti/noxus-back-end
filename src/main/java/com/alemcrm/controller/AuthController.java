package com.alemcrm.controller;

import com.alemcrm.model.User;
import com.alemcrm.service.UserService;
import com.alemcrm.util.TokenUtil;

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

        System.out.println("Tentativa de login para email: " + email + " com senha: " + password);

        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Usuário encontrado: " + user.getEmail() + ", senha no banco: " + user.getPassword());

            if (user.getPassword().equals(password)) {
                System.out.println("Senha correta!");

                String token = TokenUtil.generateFakeToken(user.getEmail(), user.getRole());

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", user.getRole());
                response.put("name", user.getName());

                return ResponseEntity.ok(response);
            } else {
                System.out.println("Senha incorreta!");
            }
        } else {
            System.out.println("Usuário não encontrado!");
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

}
