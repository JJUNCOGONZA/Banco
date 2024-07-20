package com.banco.usuarios.service.controller;

import com.banco.usuarios.service.dto.ClienteDTO;
import com.banco.usuarios.service.dto.ClienteResponseDTO;
import com.banco.usuarios.service.model.Cliente;
import com.banco.usuarios.service.model.Persona;
import com.banco.usuarios.service.repository.ClienteRepository;
import com.banco.usuarios.service.repository.PersonaRepository;
import com.banco.usuarios.service.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ClienteService clienteService;


    // Obtener todos los clientes
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    // Obtener cliente por ID
    @GetMapping("/{id}")
    public Optional<Cliente> getClienteById(@PathVariable Integer id) {
        return clienteService.getClienteById(id);
    }

    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        // Crear un nuevo objeto Cliente
        Cliente cliente = new Cliente();
        // Buscar la persona asociada por ID
        Persona persona = personaRepository.findById(clienteDTO.getIdPersona())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Persona no encontrada"));
        // Asignar los valores del DTO al nuevo cliente
        cliente.setPersona(persona);
        cliente.setContrasena(clienteDTO.getContrasena());
        cliente.setEstado(clienteDTO.getEstado());
        // Verificar si el cliente ya existe
        Optional<Cliente> existingCliente = clienteRepository
                .findByPersona_IdPersona(cliente.getPersona().getIdPersona());
        if (existingCliente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente ya existe");
        }
        // Guardar el nuevo cliente
        Cliente savedCliente = clienteRepository.save(cliente);
        return new ResponseEntity<>(savedCliente, HttpStatus.CREATED);
    }
    
    // Actualizar parcialmente un cliente
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> partiallyUpdateCliente(@PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {
        // Buscar el cliente existente por ID
        Optional<Cliente> existingCliente = clienteRepository.findById(id);
        if (!existingCliente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        Cliente clienteToUpdate = existingCliente.get();
        // Actualizar los campos específicos del cliente
        updates.forEach((key, value) -> {
            if (key.equals("personaId")) {
                Persona persona = personaRepository.findById((Integer) value)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Persona no encontrada"));
                clienteToUpdate.setPersona(persona);
            } else {
                // Actualizar otros campos dinámicamente usando ReflectionUtils
                Field field = ReflectionUtils.findField(Cliente.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, clienteToUpdate, value);
                }
            }
        });
        // Guardar el cliente actualizado
        clienteRepository.save(clienteToUpdate);
        ClienteResponseDTO responseDTO = new ClienteResponseDTO(clienteToUpdate);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Eliminar un cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable Integer id) {
        // Buscar el cliente existente por ID
        Optional<Cliente> existingCliente = clienteRepository.findById(id);
        if (!existingCliente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        // Eliminar el cliente
        clienteRepository.deleteById(id);
        return new ResponseEntity<>("Cliente eliminado exitosamente", HttpStatus.OK);
    }
}
