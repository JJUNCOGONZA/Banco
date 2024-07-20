package com.banco.cuentas_movimientos.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SaldoInsuficienteException extends ResponseStatusException {
    public SaldoInsuficienteException() {
        super(HttpStatus.BAD_REQUEST, "Saldo no disponible");
    }
}
