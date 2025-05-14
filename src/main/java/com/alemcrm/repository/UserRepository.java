package com.alemcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	  List<User> findByCompanyId(Long companyId);
	    User findByEmail(String email);
}
