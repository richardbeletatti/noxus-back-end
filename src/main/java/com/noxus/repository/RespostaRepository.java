package com.noxus.repository;

import com.noxus.model.Resposta;
import com.noxus.model.ConversaEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    List<Resposta> findByConversaEstado(ConversaEstado conversaEstado);
}
