package com.plazoleta.pedidos.infrastructure.endpoint;

import com.plazoleta.pedidos.application.dto.AsignarPedidoResponse;
import com.plazoleta.pedidos.application.dto.CancelarPedidoResponse;
import com.plazoleta.pedidos.application.dto.EntregarPedidoRequest;
import com.plazoleta.pedidos.application.dto.EntregarPedidoResponse;
import com.plazoleta.pedidos.application.dto.PedidoPageResponse;
import com.plazoleta.pedidos.application.dto.PedidoRequest;
import com.plazoleta.pedidos.application.dto.PedidoResponse;
import com.plazoleta.pedidos.application.handle.AsignarPedidoHandle;
import com.plazoleta.pedidos.application.handle.CancelarPedidoHandle;
import com.plazoleta.pedidos.application.handle.CrearPedidoHandle;
import com.plazoleta.pedidos.application.handle.EntregarPedidoHandle;
import com.plazoleta.pedidos.application.handle.ListarPedidosPorEstadoHandle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gestion de pedidos")
public class PedidoController {

    private final CrearPedidoHandle crearPedidoHandle;
    private final ListarPedidosPorEstadoHandle listarPedidosPorEstadoHandle;
    private final AsignarPedidoHandle asignarPedidoHandle;
    private final CancelarPedidoHandle cancelarPedidoHandle;
    private final EntregarPedidoHandle entregarPedidoHandle;

    public PedidoController(CrearPedidoHandle crearPedidoHandle,
                            ListarPedidosPorEstadoHandle listarPedidosPorEstadoHandle,
                            AsignarPedidoHandle asignarPedidoHandle,
                            CancelarPedidoHandle cancelarPedidoHandle,
                            EntregarPedidoHandle entregarPedidoHandle) {
        this.crearPedidoHandle = crearPedidoHandle;
        this.listarPedidosPorEstadoHandle = listarPedidosPorEstadoHandle;
        this.asignarPedidoHandle = asignarPedidoHandle;
        this.cancelarPedidoHandle = cancelarPedidoHandle;
        this.entregarPedidoHandle = entregarPedidoHandle;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Realizar pedido", description = "Crea un nuevo pedido para el cliente autenticado.")
    @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente")
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody PedidoRequest request,
                                                      Authentication authentication) {
        PedidoResponse response = crearPedidoHandle.crearPedido(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLEADO')")
    @Operation(summary = "Listar pedidos por estado",
            description = "Lista los pedidos del restaurante del empleado autenticado, opcionalmente filtrados por estado.")
    @ApiResponse(responseCode = "200", description = "Listado exitoso")
    public ResponseEntity<PedidoPageResponse> listarPedidos(
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        PedidoPageResponse response = listarPedidosPorEstadoHandle.listar(estado, page, size, authentication);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/asignar")
    @PreAuthorize("hasRole('EMPLEADO')")
    @Operation(summary = "Asignarse a un pedido",
            description = "El empleado se asigna a un pedido. Cambia el estado de PENDIENTE a EN_PREPARACION.")
    @ApiResponse(responseCode = "200", description = "Pedido asignado exitosamente")
    @ApiResponse(responseCode = "400", description = "El pedido no esta en estado PENDIENTE o ya tiene empleado")
    @ApiResponse(responseCode = "403", description = "El pedido no pertenece al restaurante del empleado")
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    public ResponseEntity<AsignarPedidoResponse> asignarPedido(
            @PathVariable Long id,
            Authentication authentication) {
        AsignarPedidoResponse response = asignarPedidoHandle.asignar(id, authentication);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Cancelar pedido",
            description = "El cliente cancela su pedido. Solo pedidos en estado PENDIENTE.")
    @ApiResponse(responseCode = "200", description = "Pedido cancelado exitosamente")
    @ApiResponse(responseCode = "400", description = "El pedido ya esta en preparacion")
    @ApiResponse(responseCode = "403", description = "El pedido no pertenece al cliente")
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    public ResponseEntity<CancelarPedidoResponse> cancelarPedido(
            @PathVariable Long id,
            Authentication authentication) {
        CancelarPedidoResponse response = cancelarPedidoHandle.cancelar(id, authentication);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/entregar")
    @PreAuthorize("hasRole('EMPLEADO')")
    @Operation(summary = "Entregar pedido",
            description = "El empleado marca el pedido como entregado. Valida PIN y estado LISTO.")
    @ApiResponse(responseCode = "200", description = "Pedido entregado exitosamente")
    @ApiResponse(responseCode = "400", description = "El pedido no esta en estado LISTO o PIN incorrecto")
    @ApiResponse(responseCode = "403", description = "El pedido no pertenece al restaurante del empleado")
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    public ResponseEntity<EntregarPedidoResponse> entregarPedido(
            @PathVariable Long id,
            @RequestBody EntregarPedidoRequest request,
            Authentication authentication) {
        EntregarPedidoResponse response = entregarPedidoHandle.entregar(id, request, authentication);
        return ResponseEntity.ok(response);
    }
}