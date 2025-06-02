package com.alemcrm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alemcrm.dto.KanbanCardDTO;
import com.alemcrm.dto.KanbanColumnDTO;
import com.alemcrm.model.KanbanCard;
import com.alemcrm.model.KanbanColumn;
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

    @PostMapping("/admin/kanban/columns/{columnId}/cards")
    public KanbanCard createCard(@PathVariable Long columnId, @RequestBody KanbanCard cardData) {
        return kanbanService.createCard(columnId, cardData);
    }

}
