package com.noxus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noxus.model.Client;
import com.noxus.model.ConversaEstado;

public interface ConversaEstadoRepository extends JpaRepository<ConversaEstado, Long> {
    Optional<ConversaEstado> findByCliente(Client cliente);
}
