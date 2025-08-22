package com.noxus.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noxus.model.User;
import com.noxus.service.UserService;
import com.noxus.util.TokenUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    public AuthController(UserService userService, BCryptPasswordEncoder passwordEncoder, TokenUtil tokenUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenUtil = tokenUtil;
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

            if (passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("Senha correta!");

                String token = tokenUtil.generateToken(user.getId(), user.getEmail(),user.getName(),
                		user.getRole());

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
