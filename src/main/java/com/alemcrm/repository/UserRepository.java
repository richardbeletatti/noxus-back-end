package com.alemcrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByRole(String role);

	Optional<User> findByEmail(String email);

}
