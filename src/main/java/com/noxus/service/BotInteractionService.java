package com.noxus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noxus.model.KanbanCard;
import com.noxus.model.KanbanColumn;
import com.noxus.repository.KanbanCardRepository;
import com.noxus.repository.KanbanColumnRepository;

@Service
public class BotInteractionService {

	@Autowired
	private KanbanColumnRepository kanbanColumnRepository;

	@Autowired
	private KanbanCardRepository kanbanCardRepository;

	public void salvarRespostasEAdicionarCard(String userPhone,
                                          String nomeUsuario,
                                          String intencao,
                                          String descricao,
                                          Long userId) {
    // 1 - Encontrar a coluna que corresponde à intenção e ao usuário
    KanbanColumn coluna = kanbanColumnRepository
            .findByNameIgnoreCaseAndUserId(intencao, userId)
            .orElseThrow(() -> new RuntimeException(
                    "Coluna não encontrada para a intenção: " + intencao + " e usuário: " + userId
            ));

    // 2 - Criar o card
    KanbanCard novoCard = new KanbanCard();
    novoCard.setTitle(nomeUsuario);
    novoCard.setDescription(descricao + "\nTelefone: " + userPhone);
    novoCard.setColumn(coluna);

    // 3 - Salvar no banco
    kanbanCardRepository.save(novoCard);
}

}
