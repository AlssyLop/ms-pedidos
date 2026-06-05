package com.plazoleta.pedidos.infrastructure.endpoint;

import com.plazoleta.pedidos.application.dto.PedidoRequest;
import com.plazoleta.pedidos.application.dto.PedidoResponse;
import com.plazoleta.pedidos.application.handle.CrearPedidoHandle;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final CrearPedidoHandle crearPedidoHandle;

    public PedidoController(CrearPedidoHandle crearPedidoHandle) {
        this.crearPedidoHandle = crearPedidoHandle;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody PedidoRequest request,
                                                      Authentication authentication) {
        PedidoResponse response = crearPedidoHandle.crearPedido(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
