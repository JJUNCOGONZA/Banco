package com.banco.usuarios;


import com.banco.usuarios.service.controller.PersonaController;
import com.banco.usuarios.service.model.Persona;
import com.banco.usuarios.service.repository.PersonaRepository;
import com.banco.usuarios.service.service.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PersonaControllerTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private PersonaService personaService;

    @InjectMocks
    private PersonaController personaController;

    private Persona persona;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        persona = new Persona();
        persona.setNombre("Jeisson Junco");
        persona.setGenero("Masculino");
        persona.setEdad(30);
        persona.setIdentificacion("123456787");
        persona.setDireccion("Calle 123");
        persona.setTelefono("987654321");
    }

    @Test
    void testGetAllPersonas() {
        List<Persona> personas = Arrays.asList(persona);
        when(personaService.getAllPersonas()).thenReturn(personas);

        List<Persona> result = personaController.getAllPersonas();
        assertEquals(1, result.size());
        assertEquals("Jeisson Junco", result.get(0).getNombre());
    }

    @Test
    void testGetPersonaById() {
        when(personaService.getPersonaById(anyInt())).thenReturn(Optional.of(persona));

        Optional<Persona> result = personaController.getPersonaById(1);
        assertTrue(result.isPresent());
        assertEquals("Jeisson Junco", result.get().getNombre());
    }

    @Test
    void testCreatePersona() {
        System.out.println("Ejecutando testCreatePersona...");
        when(personaRepository.findByIdentificacion(anyString())).thenReturn(Optional.empty());
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        ResponseEntity<Persona> response = personaController.createPersona(persona);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Jeisson Junco", response.getBody().getNombre());
    }

    @Test
    void testCreatePersonaAlreadyExists() {
        System.out.println("Ejecutando testCreatePersonaAlreadyExists...");
        when(personaRepository.findByIdentificacion(anyString())).thenReturn(Optional.of(persona));
    
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            personaController.createPersona(persona);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode().value());
        assertEquals("La persona ya existe", exception.getReason());
    }
    

    @Test
    void testUpdatePersona() {
        when(personaRepository.findById(anyInt())).thenReturn(Optional.of(persona));
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        Persona updatedPersona = new Persona();
        updatedPersona.setNombre("Carlos");
        updatedPersona.setGenero("Masculino");
        updatedPersona.setEdad(35);
        updatedPersona.setIdentificacion("987654321");
        updatedPersona.setDireccion("Avenida 456");
        updatedPersona.setTelefono("123456789");

        ResponseEntity<String> response = personaController.updatePersona(1, updatedPersona);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Proceso realizado con éxito", response.getBody());
    }

    @Test
    void testUpdatePersonaNotFound() {
        when(personaRepository.findById(anyInt())).thenReturn(Optional.empty());

        Persona updatedPersona = new Persona();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            personaController.updatePersona(1, updatedPersona);
        });
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        assertEquals("Persona no encontrada", exception.getReason());
    }

    @Test
    void testDeletePersona() {
        when(personaRepository.findById(anyInt())).thenReturn(Optional.of(persona));

        ResponseEntity<String> response = personaController.deletePersona(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Persona eliminada exitosamente", response.getBody());
        verify(personaRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletePersonaNotFound() {
        when(personaRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            personaController.deletePersona(1);
        });
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        assertEquals("Persona no encontrada", exception.getReason());
    }
}