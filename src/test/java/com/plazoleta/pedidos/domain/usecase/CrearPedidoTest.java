package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.model.DetallePedido;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.PlatoInfo;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.ClienteValidacionPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.RestauranteValidacionPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import com.plazoleta.pedidos.domain.model.value.ClienteInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearPedidoTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private RestauranteValidacionPort restauranteValidacion;

    @Mock
    private ClienteValidacionPort clienteValidacion;

    @Mock
    private TrazabilidadRepositoryPort trazabilidadRepository;

    @InjectMocks
    private CrearPedido crearPedido;

    private Pedido crearPedidoConDetalles(Long idCliente, Long idRestaurante, Long idPlato, int cantidad) {
        Pedido pedido = new Pedido(null, idCliente, "Cliente Test", "+573001234567",
                idRestaurante, null, null, null, null, null);
        pedido.setDetalles(List.of(new DetallePedido(null, null, idPlato, "Plato Test", cantidad)));
        return pedido;
    }

    @Test
    @DisplayName("Crear pedido exitosamente cuando cliente, restaurante y platos son validos")
    void crearPedidoExitoso() {
        ClienteInfo cliente = new ClienteInfo(1L, "Juan Perez", "+573001234567");
        Pedido pedido = crearPedidoConDetalles(1L, 10L, 100L, 2);

        when(clienteValidacion.obtenerCliente(1L)).thenReturn(Optional.of(cliente));
        when(restauranteValidacion.existsById(10L)).thenReturn(true);
        when(restauranteValidacion.obtenerInfoPlatos(10L, List.of(100L)))
                .thenReturn(List.of(new PlatoInfo(100L, "Plato Test", true)));
        when(pedidoRepository.existsByIdClienteAndEstadoIn(anyLong(), anyList())).thenReturn(false);
        when(trazabilidadRepository.save(any(Trazabilidad.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        Pedido resultado = crearPedido.crearPedido(pedido);

        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
        assertEquals("Juan Perez", resultado.getNombreCliente());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el cliente no existe")
    void clienteNoExiste() {
        Pedido pedido = crearPedidoConDetalles(1L, 10L, 100L, 2);
        when(clienteValidacion.obtenerCliente(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> crearPedido.crearPedido(pedido));
        assertEquals("El cliente no existe", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el restaurante no existe")
    void restauranteNoExiste() {
        ClienteInfo cliente = new ClienteInfo(1L, "Juan Perez", "+573001234567");
        Pedido pedido = crearPedidoConDetalles(1L, 10L, 100L, 2);

        when(clienteValidacion.obtenerCliente(1L)).thenReturn(Optional.of(cliente));
        when(restauranteValidacion.existsById(10L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> crearPedido.crearPedido(pedido));
        assertEquals("El restaurante no existe", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el plato no pertenece al restaurante")
    void platoNoPertenece() {
        ClienteInfo cliente = new ClienteInfo(1L, "Juan Perez", "+573001234567");
        Pedido pedido = crearPedidoConDetalles(1L, 10L, 100L, 2);

        when(clienteValidacion.obtenerCliente(1L)).thenReturn(Optional.of(cliente));
        when(restauranteValidacion.existsById(10L)).thenReturn(true);
        when(restauranteValidacion.obtenerInfoPlatos(10L, List.of(100L)))
                .thenReturn(List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> crearPedido.crearPedido(pedido));
        assertEquals("El plato 100 no pertenece al restaurante", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el cliente ya tiene un pedido en proceso")
    void clienteConPedidoActivo() {
        ClienteInfo cliente = new ClienteInfo(1L, "Juan Perez", "+573001234567");
        Pedido pedido = crearPedidoConDetalles(1L, 10L, 100L, 2);

        when(clienteValidacion.obtenerCliente(1L)).thenReturn(Optional.of(cliente));
        when(restauranteValidacion.existsById(10L)).thenReturn(true);
        when(restauranteValidacion.obtenerInfoPlatos(10L, List.of(100L)))
                .thenReturn(List.of(new PlatoInfo(100L, "Plato Test", true)));
        when(pedidoRepository.existsByIdClienteAndEstadoIn(1L,
                List.of(EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO)))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> crearPedido.crearPedido(pedido));
        assertEquals("Tienes un pedido en proceso", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el plato no esta activo")
    void platoNoActivo() {
        ClienteInfo cliente = new ClienteInfo(1L, "Juan Perez", "+573001234567");
        Pedido pedido = crearPedidoConDetalles(1L, 10L, 100L, 2);

        when(clienteValidacion.obtenerCliente(1L)).thenReturn(Optional.of(cliente));
        when(restauranteValidacion.existsById(10L)).thenReturn(true);
        when(restauranteValidacion.obtenerInfoPlatos(10L, List.of(100L)))
                .thenReturn(List.of(new PlatoInfo(100L, "Plato Test", false)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> crearPedido.crearPedido(pedido));
        assertEquals("El plato 100 no se encuentra disponible", ex.getMessage());
    }
}