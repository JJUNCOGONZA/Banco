package com.banco.usuarios.service.repository;

import com.banco.usuarios.service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findById(Integer id);
    List<Cliente> findAll();
    Optional<Cliente> findByPersona_IdPersona(Integer personaId);
}