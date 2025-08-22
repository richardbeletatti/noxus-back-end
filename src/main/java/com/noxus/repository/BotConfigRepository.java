package com.noxus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noxus.model.BotConfig;

public interface BotConfigRepository extends JpaRepository<BotConfig, Long> {
    Optional<BotConfig> findFirstByOrderByIdAsc();
}
