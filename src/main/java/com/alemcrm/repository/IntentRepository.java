package com.alemcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.Intent;

public interface IntentRepository extends JpaRepository<Intent, Long> {}

