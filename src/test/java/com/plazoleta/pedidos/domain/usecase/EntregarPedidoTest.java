package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.EntregarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
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
class EntregarPedidoTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private EmpleadoRestaurantePedidosPort empleadoRestaurantePort;

    @Mock
    private TrazabilidadRepositoryPort trazabilidadRepository;

    @InjectMocks
    private EntregarPedido entregarPedido;

    @Test
    @DisplayName("Entregar pedido exitosamente cuando esta en LISTO y PIN correcto")
    void entregarPedidoExitoso() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.LISTO, null, "123456", null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(trazabilidadRepository.save(any(Trazabilidad.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = entregarPedido.entregarPedido(1L, 99L, "123456");

        assertEquals(EstadoPedido.ENTREGADO, resultado.getEstado());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el empleado no esta asociado a un restaurante")
    void empleadoSinRestaurante() {
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "123456"));
        assertEquals("Empleado no asociado a ningun restaurante", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no existe")
    void pedidoNoExiste() {
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "123456"));
        assertEquals("El pedido no existe", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no pertenece al restaurante del empleado")
    void pedidoNoDelRestaurante() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 99L,
                EstadoPedido.LISTO, null, "123456", null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "123456"));
        assertEquals("No tienes permiso para entregar este pedido", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no esta en LISTO")
    void pedidoNoListo() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.EN_PREPARACION, 77L, null, null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "123456"));
        assertEquals("El pedido no se encuentra en estado LISTO", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido ya fue entregado")
    void pedidoYaEntregado() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.ENTREGADO, 77L, null, null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "123456"));
        assertEquals("El pedido ya fue entregado", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido fue cancelado")
    void pedidoCancelado() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.CANCELADO, 77L, null, null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "123456"));
        assertEquals("El pedido fue cancelado", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el PIN es incorrecto")
    void pinIncorrecto() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.LISTO, null, "123456", null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "999999"));
        assertEquals("El pin de seguridad es incorrecto", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no tiene PIN")
    void pedidoSinPin() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.LISTO, null, null, null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> entregarPedido.entregarPedido(1L, 99L, "123456"));
        assertEquals("El pin de seguridad es incorrecto", ex.getMessage());
    }
}