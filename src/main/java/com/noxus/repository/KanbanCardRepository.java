package com.noxus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noxus.model.KanbanCard;

public interface KanbanCardRepository extends JpaRepository<KanbanCard, Long> {}