package com.banco.cuentas_movimientos.service.controller;

import com.banco.cuentas_movimientos.service.dto.ReporteDTO;
import com.banco.cuentas_movimientos.service.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

     // Obtener reporte de movimientos
    @GetMapping
    public List<ReporteDTO> getReporte(
            @RequestParam Integer clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return reporteService.generarReporte(clienteId, fechaInicio, fechaFin);
    }
}
