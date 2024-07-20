package com.banco.usuarios.service.service;

import com.banco.usuarios.service.model.Cliente;
import com.banco.usuarios.service.model.Persona;
import com.banco.usuarios.service.repository.ClienteRepository;
import com.banco.usuarios.service.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public Optional<Cliente> getClienteById(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            Optional<Persona> persona = personaRepository.findById(cliente.get().getPersona().getIdPersona());
            persona.ifPresent(cliente.get()::setPersona);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        return cliente;
    }

    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tienes clientes :(");
        }
        clientes.forEach(cliente -> {
            Optional<Persona> persona = personaRepository.findById(cliente.getPersona().getIdPersona());
            persona.ifPresent(cliente::setPersona);
        });
        return clientes;
    }
}
