package com.alemcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.KanbanColumn;

public interface KanbanColumnRepository extends JpaRepository<KanbanColumn, Long> {}

