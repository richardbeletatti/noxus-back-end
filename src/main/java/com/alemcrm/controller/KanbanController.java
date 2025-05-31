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
public class KanbanController {

    private final KanbanService kanbanService;

    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @PostMapping("/columns")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KanbanColumnDTO> createColumn(@RequestBody KanbanColumn column) {
        return ResponseEntity.ok(kanbanService.createColumn(column));
    }

    @DeleteMapping("/columns/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteColumn(@PathVariable("id") Long id) {
        kanbanService.deleteColumn(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/columns")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<KanbanColumnDTO>> getAllColumns() {
        return ResponseEntity.ok(kanbanService.getAllColumns());
    }

    @PostMapping("/columns/{columnId}/cards")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<KanbanCardDTO> createCard(
            @PathVariable Long columnId,
            @RequestBody KanbanCard card) {
        return ResponseEntity.ok(kanbanService.createCard(columnId, card));
    }
}

