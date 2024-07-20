package com.banco.cuentas_movimientos.service.repository;

import com.banco.cuentas_movimientos.service.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    List<Movimiento> findByCuentaIdAndFechaBetween(Integer cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
