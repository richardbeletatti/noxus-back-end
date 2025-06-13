package com.alemcrm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alemcrm.dto.KanbanColumnDTO;
import com.alemcrm.model.KanbanCard;
import com.alemcrm.service.KanbanService;
import com.alemcrm.util.TokenUtil;

@RestController
@RequestMapping("kanban")
public class UserKanbanController {

    private final KanbanService kanbanService;
    private final TokenUtil tokenUtil;

    public UserKanbanController(KanbanService kanbanService, TokenUtil tokenUtil) {
        this.kanbanService = kanbanService;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping("/columns")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<KanbanColumnDTO>> getAllColumnsForUser(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = tokenUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(kanbanService.getAllColumnsForUser(userId));
    }

    @PostMapping("/admin/kanban/columns/{columnId}/cards")
    public KanbanCard createCard(@PathVariable Long columnId, @RequestBody KanbanCard cardData) {
        return kanbanService.createCard(columnId, cardData);
    }
}
