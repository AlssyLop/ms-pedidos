package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.ListarPedidosPorEstadoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPedidosPorEstadoTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @InjectMocks
    private ListarPedidosPorEstado listarPedidosPorEstado;

    @Test
    @DisplayName("Listar pedidos sin filtro de estado")
    void listarSinFiltro() {
        Pedido pedido = new Pedido(1L, 10L, "Juan", "+57300", 5L,
                EstadoPedido.PENDIENTE, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pedido> page = new PageImpl<>(List.of(pedido), pageable, 1);

        when(pedidoRepository.findByIdRestaurante(5L, pageable)).thenReturn(page);

        Page<Pedido> resultado = listarPedidosPorEstado.listar(5L, null, pageable);

        assertEquals(1, resultado.getTotalElements());
        verify(pedidoRepository).findByIdRestaurante(5L, pageable);
    }

    @Test
    @DisplayName("Listar pedidos con filtro de estado")
    void listarConFiltroEstado() {
        Pedido pedido = new Pedido(1L, 10L, "Juan", "+57300", 5L,
                EstadoPedido.PENDIENTE, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pedido> page = new PageImpl<>(List.of(pedido), pageable, 1);

        when(pedidoRepository.findByIdRestauranteAndEstado(5L, EstadoPedido.PENDIENTE, pageable))
                .thenReturn(page);

        Page<Pedido> resultado = listarPedidosPorEstado.listar(5L, EstadoPedido.PENDIENTE, pageable);

        assertEquals(1, resultado.getTotalElements());
        verify(pedidoRepository).findByIdRestauranteAndEstado(5L, EstadoPedido.PENDIENTE, pageable);
    }

    @Test
    @DisplayName("Listar pedidos con restaurante sin pedidos")
    void listarRestauranteSinPedidos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pedido> page = new PageImpl<>(List.of(), pageable, 0);

        when(pedidoRepository.findByIdRestaurante(99L, pageable)).thenReturn(page);

        Page<Pedido> resultado = listarPedidosPorEstado.listar(99L, null, pageable);

        assertEquals(0, resultado.getTotalElements());
    }
}