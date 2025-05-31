package com.alemcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.KanbanCard;

public interface KanbanCardRepository extends JpaRepository<KanbanCard, Long> {}