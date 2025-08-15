package com.alemcrm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.BotConfig;

public interface BotConfigRepository extends JpaRepository<BotConfig, Long> {
    Optional<BotConfig> findFirstByOrderByIdAsc();
}
