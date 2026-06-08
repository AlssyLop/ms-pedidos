package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.ConsultarTrazabilidadPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarTrazabilidadTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private TrazabilidadRepositoryPort trazabilidadRepository;

    @InjectMocks
    private ConsultarTrazabilidad consultarTrazabilidad;

    @Test
    @DisplayName("Consultar trazabilidad exitosamente cuando el pedido existe y pertenece al cliente")
    void consultarTrazabilidadExitoso() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.ENTREGADO, 77L, "123456",
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Trazabilidad t1 = new Trazabilidad("id1", 1L, 10L, 5L, "PENDIENTE", "EN_PREPARACION",
                LocalDateTime.now().minusHours(2), null, 30L);
        Trazabilidad t2 = new Trazabilidad("id2", 1L, 10L, 5L, "EN_PREPARACION", "LISTO",
                LocalDateTime.now().minusHours(1), null, 45L);
        when(trazabilidadRepository.findByIdPedidoOrderByFechaCambioAsc(1L))
                .thenReturn(List.of(t1, t2));

        List<Trazabilidad> resultado = consultarTrazabilidad.consultar(1L, 10L);

        assertEquals(2, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstadoAnterior());
        assertEquals("LISTO", resultado.get(1).getEstadoNuevo());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no existe")
    void pedidoNoExiste() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> consultarTrazabilidad.consultar(1L, 10L));
        assertEquals("El pedido no existe", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no pertenece al cliente")
    void pedidoNoDelCliente() {
        Pedido pedido = new Pedido(1L, 99L, "Cliente", "+57300", 5L,
                EstadoPedido.PENDIENTE, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> consultarTrazabilidad.consultar(1L, 10L));
        assertEquals("No tienes permiso para consultar este pedido", ex.getMessage());
    }

    @Test
    @DisplayName("Retornar lista vacia cuando no hay registros de trazabilidad")
    void trazabilidadVacia() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.PENDIENTE, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(trazabilidadRepository.findByIdPedidoOrderByFechaCambioAsc(1L))
                .thenReturn(Collections.emptyList());

        List<Trazabilidad> resultado = consultarTrazabilidad.consultar(1L, 10L);

        assertTrue(resultado.isEmpty());
    }
}
