package com.banco.cuentas_movimientos.service.service;

import com.banco.cuentas_movimientos.service.exception.SaldoInsuficienteException;
import com.banco.cuentas_movimientos.service.model.Movimiento;
import com.banco.cuentas_movimientos.service.model.Cuenta;
import com.banco.cuentas_movimientos.service.repository.MovimientoRepository;
import com.banco.cuentas_movimientos.service.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Transactional
    public Movimiento createMovimiento(Movimiento movimiento) {
        // Buscar la cuenta asociada al movimiento por ID
        Optional<Cuenta> optionalCuenta = cuentaRepository.findById(movimiento.getCuentaId());

        // Lanzar excepción si la cuenta no se encuentra
        if (!optionalCuenta.isPresent()) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }

        Cuenta cuenta = optionalCuenta.get();
        // Calcular el nuevo saldo de la cuenta
        double nuevoSaldo = cuenta.getSaldoInicial() + movimiento.getValor();

        // Verificar si hay saldo suficiente para un retiro
        if (movimiento.getValor() < 0 && nuevoSaldo < 0) {
            throw new SaldoInsuficienteException();
        }

        // Actualizar el saldo inicial de la cuenta
        cuenta.setSaldoInicial(nuevoSaldo);

        // Establecer la fecha y el nuevo saldo en el movimiento
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setSaldo(nuevoSaldo);
        // Guardar la cuenta actualizada
        cuentaRepository.save(cuenta);
        // Guardar el nuevo movimiento
        return movimientoRepository.save(movimiento);
    }
}
