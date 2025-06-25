package com.alemcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alemcrm.model.KanbanColumn;

public interface KanbanColumnRepository extends JpaRepository<KanbanColumn, Long> {
    List<KanbanColumn> findByUserId(Long userId);
    
    @Query("SELECT DISTINCT c FROM KanbanColumn c LEFT JOIN FETCH c.cards WHERE c.user.id = :userId")
    List<KanbanColumn> findByUserIdWithCards(@Param("userId") Long userId);
}

