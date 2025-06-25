package com.alemcrm.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alemcrm.dto.KanbanCardDTO;
import com.alemcrm.dto.KanbanCardRequestDTO;
import com.alemcrm.dto.KanbanColumnDTO;
import com.alemcrm.model.KanbanCard;
import com.alemcrm.model.KanbanColumn;
import com.alemcrm.model.User;
import com.alemcrm.repository.KanbanCardRepository;
import com.alemcrm.repository.KanbanColumnRepository;
import com.alemcrm.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class KanbanService {

    private final KanbanColumnRepository columnRepo;
    private final KanbanCardRepository cardRepo;
    private final UserRepository userRepo;

    public KanbanService(KanbanColumnRepository columnRepo, 
    		KanbanCardRepository cardRepo, UserRepository userRepo) {
        this.columnRepo = columnRepo;
        this.cardRepo = cardRepo;
        this.userRepo = userRepo;
    }
    
    public List<KanbanColumnDTO> getAllColumns() {
        List<KanbanColumn> columns = columnRepo.findAll();
        return columns.stream().map(KanbanColumnDTO::new).toList();
    }

    public List<KanbanColumnDTO> getAllColumnsForUser(Long userId) {
    	List<KanbanColumn> columns = columnRepo.findByUserIdWithCards(userId);
        return columns.stream()
                      .map(this::toColumnDTO)
                      .toList();
    }
    
    public KanbanColumnDTO createColumnForUser(Long userId, KanbanColumn column) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        column.setUser(user);

        KanbanColumn saved = columnRepo.save(column);
        return toColumnDTO(saved);
    }

    public void deleteColumnForUser(Long userId, Long columnId) {
        KanbanColumn column = columnRepo.findById(columnId)
            .orElseThrow(() -> new RuntimeException("Coluna não encontrada"));

        if (column.getUser() == null || !column.getUser().getId().equals(userId)) {
            throw new RuntimeException("Coluna não pertence ao usuário");
        }

        columnRepo.deleteById(columnId);
    }

    public KanbanCard createCardFromDTO(KanbanCardRequestDTO dto) {
        KanbanColumn column = columnRepo.findById(dto.getColumnId())
            .orElseThrow(() -> new RuntimeException("Coluna não encontrada"));

        KanbanCard card = new KanbanCard();
        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        card.setPhoneNumber(dto.getPhoneNumber());
        card.setColumn(column);

        return cardRepo.save(card);
    }

    private KanbanColumnDTO toColumnDTO(KanbanColumn column) {
        List<KanbanCardDTO> cardDTOs = column.getCards().stream()
                .map(this::toCardDTO)
                .toList();

        return new KanbanColumnDTO(column.getId(), column.getName(), cardDTOs);
    }

    private KanbanCardDTO toCardDTO(KanbanCard card) {
        return new KanbanCardDTO(card);
    }

    public KanbanCard findCardById(Long cardId) {
        return cardRepo.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));
    }
	
    @Transactional
    public void deleteCardForUser(Long cardId, Long userId) throws AccessDeniedException {
        KanbanCard card = cardRepo.findById(cardId)
            .orElseThrow(() -> new EntityNotFoundException("Card não encontrado"));

        if (!card.getColumn().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Você não tem permissão para deletar este card");
        }

        KanbanColumn column = card.getColumn();
        column.getCards().remove(card);

        cardRepo.delete(card);
    }


}
