package com.alemcrm.config;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.alemcrm.model.User;
import com.alemcrm.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Transactional
    CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            createAdminIfNotExists(userRepository);
        };
    }

    private void createAdminIfNotExists(UserRepository repository) {
        String adminEmail = "admin@noxus.com";
        Optional<User> userOpt = repository.findByEmail(adminEmail);
        if (userOpt.isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("admin");
            repository.save(admin);
            System.out.println("Usuário administrador criado: " + adminEmail);
        } else {
            // Atualiza senha para o hash correto se quiser garantir:
            User admin = userOpt.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            repository.save(admin);
            System.out.println("Usuário administrador atualizado com senha criptografada: " + adminEmail);
        }
    }

}
