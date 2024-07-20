package com.banco.cuentas_movimientos.service.service;

import com.banco.cuentas_movimientos.service.dto.ClienteDTO;
import com.banco.cuentas_movimientos.service.dto.ReporteDTO;
import com.banco.cuentas_movimientos.service.mapper.ReporteMapper;
import com.banco.cuentas_movimientos.service.model.Cuenta;
import com.banco.cuentas_movimientos.service.model.Movimiento;
import com.banco.cuentas_movimientos.service.repository.CuentaRepository;
import com.banco.cuentas_movimientos.service.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ReporteMapper reporteMapper;

    // Generar reporte de movimientos para un cliente en un rango de fechas
    public List<ReporteDTO> generarReporte(Integer clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Obtener la información del cliente
        ClienteDTO clienteDTO = clienteService.getClienteById(clienteId);

        // Obtener todas las cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        // Generar el reporte a partir de los movimientos en las cuentas del cliente
        return cuentas.stream().flatMap(cuenta -> {
            // Obtener los movimientos de la cuenta en el rango de fechas especificado
            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(cuenta.getIdCuenta(), fechaInicio, fechaFin);
            // Convertir los movimientos a ReporteDTO usando el mapeador
            return movimientos.stream().map(movimiento -> reporteMapper.toReporteDTO(movimiento, cuenta, clienteDTO.getPersona().getNombre()));
        }).collect(Collectors.toList());
    }
}
