package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.AsignarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
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
class AsignarPedidoTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private EmpleadoRestaurantePedidosPort empleadoRestaurantePort;

    @InjectMocks
    private AsignarPedido asignarPedido;

    private Pedido crearPedidoPendiente(Long id, Long idRestaurante) {
        return new Pedido(id, 10L, "Cliente", "+57300", idRestaurante,
                EstadoPedido.PENDIENTE, null, null, null, null);
    }

    @Test
    @DisplayName("Asignar pedido exitosamente cuando esta en PENDIENTE")
    void asignarPedidoExitoso() {
        Pedido pedido = crearPedidoPendiente(1L, 5L);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = asignarPedido.asignarPedido(1L, 99L);

        assertEquals(EstadoPedido.EN_PREPARACION, resultado.getEstado());
        assertEquals(99L, resultado.getIdEmpleado());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el empleado no esta asociado a un restaurante")
    void empleadoSinRestaurante() {
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> asignarPedido.asignarPedido(1L, 99L));
        assertEquals("Empleado no asociado a ningun restaurante", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no existe")
    void pedidoNoExiste() {
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> asignarPedido.asignarPedido(1L, 99L));
        assertEquals("El pedido no existe", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido no pertenece al restaurante del empleado")
    void pedidoNoDelRestaurante() {
        Pedido pedido = crearPedidoPendiente(1L, 99L);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> asignarPedido.asignarPedido(1L, 99L));
        assertEquals("No tienes permiso para asignarte a este pedido", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido ya esta en EN_PREPARACION")
    void pedidoYaEnPreparacion() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.EN_PREPARACION, 77L, null, null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> asignarPedido.asignarPedido(1L, 99L));
        assertEquals("Pedido asignado, se encuentra en estado PREPARACION", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el pedido ya tiene un empleado asignado")
    void pedidoYaTieneEmpleado() {
        Pedido pedido = new Pedido(1L, 10L, "Cliente", "+57300", 5L,
                EstadoPedido.PENDIENTE, 77L, null, null, null);
        when(empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(99L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> asignarPedido.asignarPedido(1L, 99L));
        assertEquals("El pedido ya tiene un empleado asignado", ex.getMessage());
    }
}