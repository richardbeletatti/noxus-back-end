package com.alemcrm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alemcrm.dto.KanbanCardDTO;
import com.alemcrm.dto.KanbanColumnDTO;
import com.alemcrm.model.KanbanCard;
import com.alemcrm.service.KanbanService;

@RestController
@RequestMapping("kanban")
public class UserKanbanController {

    private final KanbanService kanbanService;

    public UserKanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @GetMapping("/columns")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<KanbanColumnDTO>> getAllColumnsForUser(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(kanbanService.getAllColumns());
    }

//    @GetMapping("/columns/{columnId}/cards")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<List<KanbanCardDTO>> getCardsForColumn(
//            @PathVariable Long columnId,
//            @RequestHeader("Authorization") String authHeader) {
//        // Opcional: extrair userId do token e passar para o service
//        return ResponseEntity.ok(kanbanService.getCardsForColumn(columnId));
//    }

    @PostMapping("/columns/{columnId}/cards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<KanbanCardDTO> createCardForUser(
            @PathVariable Long columnId,
            @RequestBody KanbanCard card,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(kanbanService.createCard(columnId, card));
    }

}
