package com.alemcrm.service;

import java.util.List;
import java.util.Optional;

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
    
    public List<User> getAllRegularUsers() {
        List<User> users = userRepository.findByRole("USER");
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
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        User existingUser = optionalUser.get();
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        // Se a senha foi enviada, atualiza-a também
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }
}
