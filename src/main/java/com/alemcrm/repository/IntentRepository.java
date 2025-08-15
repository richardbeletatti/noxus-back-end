package com.alemcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.Intent;

public interface IntentRepository extends JpaRepository<Intent, Long> {
    Intent findByNameIgnoreCase(String name);
    
    List<Intent> findByBotConfigId(Long botConfigId);
}

