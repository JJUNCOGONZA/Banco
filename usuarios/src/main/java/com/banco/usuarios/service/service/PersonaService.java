package com.banco.usuarios.service.service;

import com.banco.usuarios.service.model.Persona;
import com.banco.usuarios.service.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public List<Persona> getAllPersonas() {
        List<Persona> personas = personaRepository.findAll();
        if (personas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay personas");
        }
        return personas;
    }

    public Optional<Persona> getPersonaById(Integer id) {
        Optional<Persona> persona = personaRepository.findById(id);
        if (!persona.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        return persona;
    }
}
