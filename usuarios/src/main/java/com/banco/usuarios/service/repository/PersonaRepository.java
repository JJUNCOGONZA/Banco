package com.banco.usuarios.service.repository;

import com.banco.usuarios.service.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByIdentificacion(String identificacion);
}
