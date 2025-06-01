package com.alemcrm.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alemcrm.dto.UserDTO;
import com.alemcrm.model.User;
import com.alemcrm.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public AdminService(UserRepository userRepository, 
    					BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        System.out.println("Total de usuários encontrados: " + users.size());
        return users;
    }
    
    public User findUserById(Long id) {
        User userById = userRepository.getReferenceById(id);
        return userById;
    }
    
    public User createUser(UserDTO userDto) {
        if (userDto.getName() == null || userDto.getName().isEmpty() ||
            userDto.getEmail() == null || userDto.getEmail().isEmpty() ||
            userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encodedPassword);
        user.setRole("USER");

        return userRepository.save(user);
    }

}
