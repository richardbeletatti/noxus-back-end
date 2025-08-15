package com.alemcrm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.Client;
import com.alemcrm.model.ConversaEstado;

public interface ConversaEstadoRepository extends JpaRepository<ConversaEstado, Long> {
    Optional<ConversaEstado> findByCliente(Client cliente);
}
