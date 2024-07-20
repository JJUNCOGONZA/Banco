package com.banco.cuentas_movimientos.service.controller;

import com.banco.cuentas_movimientos.service.model.Movimiento;
import com.banco.cuentas_movimientos.service.repository.MovimientoRepository;
import com.banco.cuentas_movimientos.service.service.MovimientoService;
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
@RequestMapping("/movimientos")
public class MovimientoController {
    
    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private MovimientoService movimientoService;

    // Obtener todos los movimientos
    @GetMapping
    public ResponseEntity<?> getAllMovimientos() {
        List<Movimiento> movimientos = movimientoRepository.findAll();
        if (movimientos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron movimientos");
        }
        return new ResponseEntity<>(movimientos, HttpStatus.OK);
    }

    // Obtener un movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> getMovimientoById(@PathVariable Integer id) {
        Optional<Movimiento> movimiento = movimientoRepository.findById(id);
        if (movimiento.isPresent()) {
            return new ResponseEntity<>(movimiento.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento no encontrado");
        }
    }

    // Crear un nuevo movimiento
    @PostMapping
    public ResponseEntity<Movimiento> createMovimiento(@RequestBody Movimiento movimiento) {
        Movimiento savedMovimiento = movimientoService.createMovimiento(movimiento);
        return new ResponseEntity<>(savedMovimiento, HttpStatus.CREATED);
    }

    // Actualizar completamente un movimiento
    @PutMapping("/{id}")
    public ResponseEntity<Movimiento> updateMovimiento(@PathVariable Integer id, @RequestBody Movimiento movimiento) {
        // Verificar si el movimiento existe por ID
        Optional<Movimiento> existingMovimiento = movimientoRepository.findById(id);
        if (existingMovimiento.isPresent()) {
            // Actualizar los campos del movimiento existente
            Movimiento movimientoToUpdate = existingMovimiento.get();
            movimientoToUpdate.setCuentaId(movimiento.getCuentaId());
            movimientoToUpdate.setTipoMovimiento(movimiento.getTipoMovimiento());
            movimientoToUpdate.setValor(movimiento.getValor());
            movimientoToUpdate.setSaldo(movimiento.getSaldo());
            movimientoToUpdate.setFecha(movimiento.getFecha());
            // Guardar el movimiento actualizado
            movimientoRepository.save(movimientoToUpdate);
            return new ResponseEntity<>(movimientoToUpdate, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento no encontrado");
        }
    }

    // Actualizar parcialmente un movimiento
    @PatchMapping("/{id}")
    public ResponseEntity<Movimiento> partiallyUpdateMovimiento(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        // Verificar si el movimiento existe por ID
        Optional<Movimiento> existingMovimiento = movimientoRepository.findById(id);
        if (existingMovimiento.isPresent()) {
            Movimiento movimientoToUpdate = existingMovimiento.get();
            // Actualizar los campos específicos del movimiento
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Movimiento.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, movimientoToUpdate, value);
                }
            });
            // Guardar el movimiento parcialmente actualizado
            movimientoRepository.save(movimientoToUpdate);
            return new ResponseEntity<>(movimientoToUpdate, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento no encontrado");
        }
    }

    // Eliminar un movimiento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Integer id) {
        // Verificar si el movimiento existe por ID
        Optional<Movimiento> existingMovimiento = movimientoRepository.findById(id);
        if (existingMovimiento.isPresent()) {
            // Eliminar el movimiento
            movimientoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento no encontrado");
        }
    }
}
