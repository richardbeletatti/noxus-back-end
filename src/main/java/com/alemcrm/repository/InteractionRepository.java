package com.alemcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Long>{
    List<Interaction> findByLeadId(Long leadId);
}
