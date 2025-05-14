package com.alemcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    List<MessageRepository> findByLeadId(Long leadId);
}