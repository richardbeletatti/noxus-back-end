package com.alemcrm.config;

import com.alemcrm.model.User;
import com.alemcrm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

	@Bean
	public CommandLineRunner initData(UserRepository userRepository) {
	    return args -> {
	        if (userRepository.findByEmail("admin@noxus.com").isEmpty()) {
	            User admin = new User();
	            admin.setName("Administrador");
	            admin.setEmail("admin@noxus.com");
	            admin.setPassword("admin");
	            admin.setRole("admin");

	            userRepository.save(admin);
	            System.out.println("Usuário admin criado.");
	        }

	        if (userRepository.findByEmail("usuario@noxus.com").isEmpty()) {
	            User user = new User();
	            user.setName("Usuário Comum");
	            user.setEmail("usuario@noxus.com");
	            user.setPassword("usuario");
	            user.setRole("user");

	            userRepository.save(user);
	            System.out.println("Usuário comum criado.");
	        }
	    };
	}

}
