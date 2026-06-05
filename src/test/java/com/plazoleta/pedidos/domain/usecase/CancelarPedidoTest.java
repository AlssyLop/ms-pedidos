package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.CancelarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelarPedidoTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @InjectMocks
    private CancelarPedido cancelarPedido;

    @Test
    @DisplayName("Cancelar pedido exitosamente cuando esta en PENDIENTE")
    void cancelarPedidoExitoso() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.PENDIENTE, null, null, null, null);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = cancelarPedido.cancelarPedido(1L, 10L);

        assertEquals(EstadoPedido.CANCELADO, resultado.getEstado());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no existe")
    void pedidoNoExiste() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cancelarPedido.cancelarPedido(1L, 10L));
        assertEquals("El pedido no existe", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no pertenece al cliente")
    void pedidoNoDelCliente() {
        Pedido pedido = new Pedido(1L, 99L, "Cliente", "+57300", 5L,
                EstadoPedido.PENDIENTE, null, null, null, null);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cancelarPedido.cancelarPedido(1L, 10L));
        assertEquals("No tienes permiso para cancelar este pedido", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no esta en PENDIENTE")
    void pedidoNoPendiente() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.EN_PREPARACION, 77L, null, null, null);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cancelarPedido.cancelarPedido(1L, 10L));
        assertEquals("Lo sentimos, tu pedido ya esta en preparacion y no puede cancelarse", ex.getMessage());
    }
}