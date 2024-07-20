package com.banco.cuentas_movimientos.service.repository;

import com.banco.cuentas_movimientos.service.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {
    List<Cuenta> findByClienteId(Integer clienteId);
    boolean existsByNumeroCuenta(String numeroCuenta);
}

