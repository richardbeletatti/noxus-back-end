package com.noxus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noxus.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByTelefone(String telefone);
}
