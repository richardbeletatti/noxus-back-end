package com.alemcrm.config;

import com.alemcrm.model.User;
import com.alemcrm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            try {
                createUserIfNotExists(userRepository, 
                    "Administrador", 
                    "admin@noxus.com", 
                    "admin123",  // Nunca use senhas fracas em produção!
                    "admin"
                );

                createUserIfNotExists(userRepository,
                    "Usuário Comum",
                    "usuario@noxus.com",
                    "user123",
                    "user"
                );
            } catch (Exception e) {
                System.err.println("Erro na inicialização de dados: ");
                e.printStackTrace();
            }
        };
    }

    private void createUserIfNotExists(UserRepository repository, 
                                     String name, 
                                     String email, 
                                     String password, 
                                     String role) {
        if (repository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password); // Deveria ser um hash na prática!
            user.setRole(role);
            
            repository.save(user);
            System.out.println("Usuário criado: " + email);
        }
    }
}