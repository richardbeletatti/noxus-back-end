package com.alemcrm.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alemcrm.dto.KanbanColumnDTO;
import com.alemcrm.dto.KanbanCardDTO;
import com.alemcrm.dto.KanbanCardRequestDTO;
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

    @PostMapping("/columns/{columnId}/cards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<KanbanCardDTO> createCardForUser(
    		@PathVariable("columnId") Long columnId,
            @RequestBody KanbanCardRequestDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = tokenUtil.getUserIdFromToken(token);

        dto.setColumnId(columnId);

        KanbanCard savedCard = kanbanService.createCardFromDTO(dto);

        return ResponseEntity.ok(new KanbanCardDTO(savedCard));
    }

    @DeleteMapping("/cards/{cardId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteCardForUser(
            @PathVariable("cardId") Long cardId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = tokenUtil.getUserIdFromToken(token);

        try {
			kanbanService.deleteCardForUser(cardId, userId);
		} catch (AccessDeniedException e) {
			e.printStackTrace();
		}

        return ResponseEntity.noContent().build();
    }
}
