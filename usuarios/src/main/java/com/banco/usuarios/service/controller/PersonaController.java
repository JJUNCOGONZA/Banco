package com.banco.usuarios.service.controller;

import com.banco.usuarios.service.model.Persona;
import com.banco.usuarios.service.repository.PersonaRepository;
import com.banco.usuarios.service.service.PersonaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaService personaService;

    // Obtener todas las personas
    @GetMapping
    public List<Persona> getAllPersonas() {
        return personaService.getAllPersonas();
    }

    // Obtener una persona por ID
    @GetMapping("/{id}")
    public Optional<Persona> getPersonaById(@PathVariable Integer id) {
        return personaService.getPersonaById(id);
    }

    // Crear una nueva persona
    @PostMapping
    public ResponseEntity<Persona> createPersona(@Valid @RequestBody Persona persona) {
        Optional<Persona> existingPersona = personaRepository.findByIdentificacion(persona.getIdentificacion());
        if (existingPersona.isPresent()) {
            // Lanzar excepción si la persona ya existe
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La persona ya existe");
        }
        Persona savedPersona = personaRepository.save(persona);
        return new ResponseEntity<>(savedPersona, HttpStatus.CREATED);
    }

    // Actualizar completamente una persona existente
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePersona(@PathVariable Integer id, @Valid @RequestBody Persona persona) {
        // Verificar si la persona existe por su ID
        Optional<Persona> existingPersona = personaRepository.findById(id);
        if (!existingPersona.isPresent()) {
            // Lanzar excepción si la persona no se encuentra
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        // Actualizar los campos de la persona existente
        Persona updatedPersona = existingPersona.get();
        updatedPersona.setNombre(persona.getNombre());
        updatedPersona.setGenero(persona.getGenero());
        updatedPersona.setEdad(persona.getEdad());
        updatedPersona.setIdentificacion(persona.getIdentificacion());
        updatedPersona.setDireccion(persona.getDireccion());
        updatedPersona.setTelefono(persona.getTelefono());
        // Guardar la persona actualizada
        personaRepository.save(updatedPersona);
        return new ResponseEntity<>("Proceso realizado con éxito", HttpStatus.OK);
    }

    // Actualizar parcialmente una persona existente
    @PatchMapping("/{id}")
    public ResponseEntity<String> partiallyUpdatePersona(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        // Verificar si la persona existe por su ID
        Optional<Persona> existingPersona = personaRepository.findById(id);
        if (!existingPersona.isPresent()) {
            // Lanzar excepción si la persona no se encuentra
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        // Actualizar los campos específicos de la persona
        Persona personaToUpdate = existingPersona.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Persona.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, personaToUpdate, value);
            }
        });
        // Guardar la persona parcialmente actualizada
        personaRepository.save(personaToUpdate);
        return new ResponseEntity<>("Proceso realizado con éxito", HttpStatus.OK);
    }

    // Eliminar una persona por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePersona(@PathVariable Integer id) {
        // Verificar si la persona existe por su ID
        Optional<Persona> existingPersona = personaRepository.findById(id);
        if (!existingPersona.isPresent()) {
            // Lanzar excepción si la persona no se encuentra
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        // Eliminar la persona
        personaRepository.deleteById(id);
        return new ResponseEntity<>("Persona eliminada exitosamente", HttpStatus.OK);
    }
}
