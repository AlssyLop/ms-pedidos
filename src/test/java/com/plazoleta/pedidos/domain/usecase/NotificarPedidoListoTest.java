package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.NotificarPedidoListoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import com.plazoleta.pedidos.domain.spi.NotificacionClientePort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificarPedidoListoTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private EmpleadoRestaurantePedidosPort empleadoRestaurantePort;

    @Mock
    private NotificacionClientePort notificacionClientePort;

    @Mock
    private TrazabilidadRepositoryPort trazabilidadRepository;

    @InjectMocks
    private NotificarPedidoListo notificarPedidoListo;

    @Test
    @DisplayName("Notificar pedido listo exitosamente y enviar SMS")
    void notificarExitoso() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+573001234567", 5L,
                EstadoPedido.EN_PREPARACION, 77L, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(77L)).thenReturn(Optional.of(5L));
        when(notificacionClientePort.enviarNotificacion(eq(1L), eq("+573001234567"), any()))
                .thenReturn(true);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(trazabilidadRepository.save(any(Trazabilidad.class))).thenAnswer(inv -> inv.getArgument(0));

        String resultado = notificarPedidoListo.notificar(1L, 77L);

        assertEquals("Pedido marcado como LISTO y cliente notificado", resultado);
        assertEquals(EstadoPedido.LISTO, pedido.getEstado());
        assertNotNull(pedido.getPin());
        assertTrue(pedido.getPin().matches("\\d{6}"));
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Marcar como LISTO aunque el SMS falle")
    void smsFalla() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+573001234567", 5L,
                EstadoPedido.EN_PREPARACION, 77L, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(77L)).thenReturn(Optional.of(5L));
        when(notificacionClientePort.enviarNotificacion(eq(1L), eq("+573001234567"), any()))
                .thenReturn(false);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(trazabilidadRepository.save(any(Trazabilidad.class))).thenAnswer(inv -> inv.getArgument(0));

        String resultado = notificarPedidoListo.notificar(1L, 77L);

        assertEquals("Pedido marcado como LISTO, pero no se pudo notificar al cliente", resultado);
        assertEquals(EstadoPedido.LISTO, pedido.getEstado());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no existe")
    void pedidoNoExiste() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificarPedidoListo.notificar(1L, 77L));
        assertEquals("El pedido no existe", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no esta EN_PREPARACION")
    void pedidoNoEnPreparacion() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.PENDIENTE, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificarPedidoListo.notificar(1L, 77L));
        assertEquals("El pedido no se encuentra en estado EN_PREPARACION", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido ya esta listo")
    void pedidoYaListo() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.LISTO, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificarPedidoListo.notificar(1L, 77L));
        assertEquals("El pedido ya esta listo", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido ya fue entregado")
    void pedidoYaEntregado() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.ENTREGADO, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificarPedidoListo.notificar(1L, 77L));
        assertEquals("El pedido ya fue entregado", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido fue cancelado")
    void pedidoCancelado() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.CANCELADO, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificarPedidoListo.notificar(1L, 77L));
        assertEquals("El pedido fue cancelado", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no pertenece al restaurante del empleado")
    void pedidoNoDelRestaurante() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 99L,
                EstadoPedido.EN_PREPARACION, 77L, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(77L)).thenReturn(Optional.of(5L));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificarPedidoListo.notificar(1L, 77L));
        assertEquals("No tienes permiso para modificar este pedido", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el empleado no tiene restaurante")
    void empleadoSinRestaurante() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.EN_PREPARACION, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(77L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificarPedidoListo.notificar(1L, 77L));
        assertEquals("El empleado no esta asociado a ningun restaurante", ex.getMessage());
    }
}
