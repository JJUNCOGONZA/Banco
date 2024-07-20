package com.banco.cuentas_movimientos.service.mapper;

import com.banco.cuentas_movimientos.service.dto.ReporteDTO;
import com.banco.cuentas_movimientos.service.model.Cuenta;
import com.banco.cuentas_movimientos.service.model.Movimiento;

import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    default ReporteDTO toReporteDTO(Movimiento movimiento, Cuenta cuenta, String clienteNombre) {
        ReporteDTO reporteDTO = new ReporteDTO();
        reporteDTO.setFecha(movimiento.getFecha().format(formatter));
        reporteDTO.setCliente(clienteNombre);
        reporteDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
        reporteDTO.setTipo(cuenta.getTipoCuenta());
        reporteDTO.setSaldoInicial(cuenta.getSaldoInicial());
        reporteDTO.setEstado(cuenta.getEstado());
        reporteDTO.setMovimiento(movimiento.getValor());
        reporteDTO.setSaldoDisponible(movimiento.getSaldo());

        return reporteDTO;
    }
}
