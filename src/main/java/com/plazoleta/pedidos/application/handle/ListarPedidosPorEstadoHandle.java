package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.PedidoListadoResponse;
import com.plazoleta.pedidos.application.dto.PedidoPageResponse;
import com.plazoleta.pedidos.application.dto.PlatoPedidoListadoResponse;
import com.plazoleta.pedidos.domain.api.ListarPedidosPorEstadoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListarPedidosPorEstadoHandle {

    private final ListarPedidosPorEstadoPort listarPedidosPort;
    private final EmpleadoRestaurantePedidosPort empleadoRestaurantePort;

    public ListarPedidosPorEstadoHandle(ListarPedidosPorEstadoPort listarPedidosPort,
                                         EmpleadoRestaurantePedidosPort empleadoRestaurantePort) {
        this.listarPedidosPort = listarPedidosPort;
        this.empleadoRestaurantePort = empleadoRestaurantePort;
    }

    public PedidoPageResponse listar(String estado, int page, int size, Authentication authentication) {
        Long idEmpleado = (Long) authentication.getPrincipal();

        Long idRestaurante = empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Empleado no asociado a ningun restaurante"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        EstadoPedido estadoPedido = null;
        if (estado != null && !estado.isBlank()) {
            try {
                estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado invalido");
            }
        }

        Page<Pedido> pedidosPage = listarPedidosPort.listar(idRestaurante, estadoPedido, pageable);

        List<PedidoListadoResponse> contenido = pedidosPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PedidoPageResponse(contenido, pedidosPage.getNumber(),
                pedidosPage.getTotalPages(), pedidosPage.getTotalElements());
    }

    private PedidoListadoResponse toResponse(Pedido pedido) {
        PedidoListadoResponse r = new PedidoListadoResponse();
        r.setId(pedido.getId());
        r.setIdCliente(pedido.getIdCliente());
        r.setNombreCliente(pedido.getNombreCliente());
        r.setCelular(pedido.getCelular());
        r.setIdRestaurante(pedido.getIdRestaurante());
        r.setEstado(pedido.getEstado().name());
        r.setFechaCreacion(pedido.getFechaCreacion());
        if (pedido.getDetalles() != null) {
            r.setPlatos(pedido.getDetalles().stream()
                    .map(d -> new PlatoPedidoListadoResponse(d.getIdPlato(), d.getNombrePlato(), d.getCantidad()))
                    .toList());
        }
        return r;
    }
}