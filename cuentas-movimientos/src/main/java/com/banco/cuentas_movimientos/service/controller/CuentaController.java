package com.banco.cuentas_movimientos.service.controller;

import com.banco.cuentas_movimientos.service.dto.ClienteDTO;
import com.banco.cuentas_movimientos.service.model.Cuenta;
import com.banco.cuentas_movimientos.service.repository.CuentaRepository;
import com.banco.cuentas_movimientos.service.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteService clienteService;

    // Obtener todas las cuentas
    @GetMapping
    public ResponseEntity<List<Cuenta>> getAllCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        if (cuentas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron cuentas");
        }
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }
    
    // Obtener una cuenta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable Integer id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        if (cuenta.isPresent()) {
            return new ResponseEntity<>(cuenta.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada");
        }
    }

    // Crear una nueva cuenta
    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody Cuenta cuenta) {
        // Verificar si el cliente existe
        ClienteDTO clienteDTO = clienteService.getClienteById(cuenta.getClienteId());
        if (clienteDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente no encontrado");
        }
        // Verificar si el número de cuenta ya existe
        if (cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de cuenta ya existe");
        }
        // Guardar la nueva cuenta en el repositorio
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return new ResponseEntity<>(savedCuenta, HttpStatus.CREATED);
    }

    // Actualizar completamente una cuenta
    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> updateCuenta(@PathVariable Integer id, @RequestBody Cuenta cuenta) {
        // Verificar si el cliente existe
        ClienteDTO clienteDTO = clienteService.getClienteById(cuenta.getClienteId());
        if (clienteDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente no encontrado");
        }
        // Verificar si la cuenta existe por ID
        Optional<Cuenta> existingCuenta = cuentaRepository.findById(id);
        if (existingCuenta.isPresent()) {
            if (!existingCuenta.get().getNumeroCuenta().equals(cuenta.getNumeroCuenta()) &&
                    cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de cuenta ya existe");
            }
            // Actualizar los campos de la cuenta existente
            Cuenta cuentaToUpdate = existingCuenta.get();
            cuentaToUpdate.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaToUpdate.setTipoCuenta(cuenta.getTipoCuenta());
            cuentaToUpdate.setSaldoInicial(cuenta.getSaldoInicial());
            cuentaToUpdate.setEstado(cuenta.getEstado());
            cuentaToUpdate.setClienteId(cuenta.getClienteId());
            cuentaRepository.save(cuentaToUpdate);
            return new ResponseEntity<>(cuentaToUpdate, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada");
        }
    }

    // Actualizar parcialmente una cuenta
    @PatchMapping("/{id}")
    public ResponseEntity<Cuenta> partiallyUpdateCuenta(@PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {
        Optional<Cuenta> existingCuenta = cuentaRepository.findById(id);
        if (existingCuenta.isPresent()) {
            Cuenta cuentaToUpdate = existingCuenta.get();
            updates.forEach((key, value) -> {
                if ("clienteId".equals(key)) {
                     // Verificar si el cliente asociado existe
                    ClienteDTO clienteDTO = clienteService.getClienteById((Integer) value);
                    if (clienteDTO == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente no encontrado");
                    }
                }
                if ("numeroCuenta".equals(key)) {
                    // Verificar si el número de cuenta ya existe
                    if (!cuentaToUpdate.getNumeroCuenta().equals(value) &&
                            cuentaRepository.existsByNumeroCuenta((String) value)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de cuenta ya existe");
                    }
                }
                // Actualizar otros campos dinámicamente usando ReflectionUtils
                Field field = ReflectionUtils.findField(Cuenta.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, cuentaToUpdate, value);
                }
            });
            // Guardar la cuenta actualizada
            cuentaRepository.save(cuentaToUpdate);
            return new ResponseEntity<>(cuentaToUpdate, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada");
        }
    }

    // Eliminar una cuenta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCuenta(@PathVariable Integer id) {
        // Verificar si la cuenta existe por ID
        Optional<Cuenta> existingCuenta = cuentaRepository.findById(id);
        if (existingCuenta.isPresent()) {
            // Eliminar la cuenta
            cuentaRepository.deleteById(id);
            return new ResponseEntity<>("Cuenta eliminada exitosamente", HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada");
        }
    }
}
