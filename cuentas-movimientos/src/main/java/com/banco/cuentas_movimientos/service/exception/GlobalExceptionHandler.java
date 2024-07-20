package com.banco.cuentas_movimientos.service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("status", ex.getStatusCode().value());
        errorAttributes.put("error", ex.getStatusCode().toString());
        errorAttributes.put("message", ex.getReason());
        errorAttributes.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorAttributes, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorAttributes.put("error", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorAttributes.put("message", ex.getMessage());
        errorAttributes.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorAttributes, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
