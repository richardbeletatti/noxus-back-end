package com.alemcrm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alemcrm.dto.KanbanCardDTO;
import com.alemcrm.dto.KanbanColumnDTO;
import com.alemcrm.model.KanbanCard;
import com.alemcrm.model.KanbanColumn;
import com.alemcrm.repository.KanbanCardRepository;
import com.alemcrm.repository.KanbanColumnRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class KanbanService {

    private final KanbanColumnRepository columnRepo;
    private final KanbanCardRepository cardRepo;

    public KanbanService(KanbanColumnRepository columnRepo, KanbanCardRepository cardRepo) {
        this.columnRepo = columnRepo;
        this.cardRepo = cardRepo;
    }

    public KanbanColumnDTO createColumn(KanbanColumn column) {
        KanbanColumn saved = columnRepo.save(column);
        return toColumnDTO(saved);
    }

    public void deleteColumn(Long id) {
        columnRepo.deleteById(id);
    }

    public List<KanbanColumnDTO> getAllColumns() {
        List<KanbanColumn> columns = columnRepo.findAll();
        return columns.stream()
                .map(this::toColumnDTO)
                .toList();
    }

    public KanbanCardDTO createCard(Long columnId, KanbanCard card) {
        KanbanColumn column = columnRepo.findById(columnId)
                .orElseThrow(() -> new EntityNotFoundException("Coluna não encontrada"));
        card.setColumn(column);
        KanbanCard saved = cardRepo.save(card);
        return toCardDTO(saved);
    }

    private KanbanColumnDTO toColumnDTO(KanbanColumn column) {
        List<KanbanCardDTO> cardDTOs = column.getCards().stream()
                .map(this::toCardDTO)
                .toList();

        return new KanbanColumnDTO(column.getId(), column.getName(), cardDTOs);
    }

    private KanbanCardDTO toCardDTO(KanbanCard card) {
        return new KanbanCardDTO(card.getId(), card.getTitle(), card.getDescription());
    }
}

