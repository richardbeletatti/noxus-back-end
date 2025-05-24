package com.alemcrm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alemcrm.model.User;
import com.alemcrm.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
