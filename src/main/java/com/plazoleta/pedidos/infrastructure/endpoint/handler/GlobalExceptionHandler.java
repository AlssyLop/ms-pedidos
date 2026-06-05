package com.plazoleta.pedidos.infrastructure.endpoint.handler;

import com.plazoleta.pedidos.application.exception.PedidoNoEncontradoException;
import com.plazoleta.pedidos.application.exception.ValidacionException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<Map<String, String>> handleValidacion(ValidacionException e) {
        return ResponseEntity.badRequest()
                .body(Map.of(e.getCampo(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", e.getMessage()));
    }

    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(PedidoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", e.getMessage()));
    }
}
