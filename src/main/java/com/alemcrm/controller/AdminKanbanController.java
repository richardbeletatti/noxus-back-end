package com.alemcrm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alemcrm.dto.KanbanCardDTO;
import com.alemcrm.dto.KanbanColumnDTO;
import com.alemcrm.model.KanbanCard;
import com.alemcrm.model.KanbanColumn;
import com.alemcrm.service.KanbanService;

@RestController
@RequestMapping("admin/kanban")
public class AdminKanbanController {

    private final KanbanService kanbanService;

    public AdminKanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }
    
    @GetMapping("/users/{id}/columns") 
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<KanbanColumnDTO>> getAllColumnsForUser(
    		@PathVariable("id") Long userId) {
        return ResponseEntity.ok(kanbanService.getAllColumnsForUser(userId));
    }

    @PostMapping("/users/{id}/columns")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KanbanColumnDTO> createColumnForUser(
    		@PathVariable("id") Long userId,
            @RequestBody KanbanColumn column) {
    	System.out.println("Recebido: " + column);
        return ResponseEntity.ok(kanbanService.createColumnForUser(userId, column));
    }


    @DeleteMapping("/users/{userId}/columns/{columnId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteColumnForUser(
        @PathVariable("userId") Long userId, 
        @PathVariable("columnId") Long columnId) {
        
        kanbanService.deleteColumnForUser(userId, columnId);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/columns/{columnId}/cards")
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    public ResponseEntity<KanbanCard> createCard(
//            @PathVariable Long columnId,
//            @RequestBody KanbanCard card) {
//        return ResponseEntity.ok(kanbanService.createCard(columnId, card));
//    }
}

