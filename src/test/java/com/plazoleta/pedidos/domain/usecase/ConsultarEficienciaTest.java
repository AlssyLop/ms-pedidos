package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.ConsultarEficienciaPort;
import com.plazoleta.pedidos.domain.model.Eficiencia;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.RankingEmpleado;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.PropietarioRestaurantePort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarEficienciaTest {

    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private PropietarioRestaurantePort propietarioRestaurantePort;

    @Mock
    private TrazabilidadRepositoryPort trazabilidadRepository;

    @InjectMocks
    private ConsultarEficiencia consultarEficiencia;

    @Test
    @DisplayName("Consultar eficiencia exitosamente con pedidos entregados")
    void consultarEficienciaExitoso() {
        when(propietarioRestaurantePort.obtenerIdRestauranteDelPropietario(1L))
                .thenReturn(Optional.of(5L));

        LocalDateTime inicio = LocalDateTime.now().minusHours(2);
        LocalDateTime fin = LocalDateTime.now();
        Pedido p1 = new Pedido(10L, 100L, "Cliente", "+57300", 5L,
                EstadoPedido.ENTREGADO, 77L, null, inicio, fin);
        Pedido p2 = new Pedido(11L, 101L, "Cliente2", "+57301", 5L,
                EstadoPedido.ENTREGADO, 77L, null, inicio.plusMinutes(10), fin.plusMinutes(10));
        when(pedidoRepository.findAllByIdRestauranteAndEstado(5L, EstadoPedido.ENTREGADO))
                .thenReturn(List.of(p1, p2));

        Trazabilidad t1 = new Trazabilidad("id1", 10L, 100L, 5L, "PENDIENTE", "EN_PREPARACION",
                inicio.plusMinutes(20), 77L, 20L);
        Trazabilidad t2 = new Trazabilidad("id2", 10L, 100L, 5L, "EN_PREPARACION", "LISTO",
                fin.minusMinutes(10), 77L, 30L);
        Trazabilidad t3 = new Trazabilidad("id3", 10L, 100L, 5L, "LISTO", "ENTREGADO",
                fin, 77L, 10L);
        Trazabilidad t4 = new Trazabilidad("id4", 11L, 101L, 5L, "PENDIENTE", "ENTREGADO",
                fin.plusMinutes(10), 77L, 50L);
        when(trazabilidadRepository.findByIdPedidoInOrderByFechaCambioAsc(List.of(10L, 11L)))
                .thenReturn(List.of(t1, t2, t3, t4));

        Eficiencia resultado = consultarEficiencia.consultar(1L);

        assertEquals(2, resultado.getPedidos().size());
        assertEquals(60L, resultado.getPedidos().get(0).getTiempoTotalMinutos());
        assertEquals(50L, resultado.getPedidos().get(1).getTiempoTotalMinutos());

        List<RankingEmpleado> ranking = resultado.getRankingEmpleados();
        assertEquals(1, ranking.size());
        assertEquals(77L, ranking.get(0).getIdEmpleado());
        assertEquals(55.0, ranking.get(0).getTiempoPromedioMinutos(), 0.01);
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el propietario no tiene restaurante")
    void propietarioSinRestaurante() {
        when(propietarioRestaurantePort.obtenerIdRestauranteDelPropietario(1L))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> consultarEficiencia.consultar(1L));
        assertEquals("No se encontro un restaurante para el propietario", ex.getMessage());
    }

    @Test
    @DisplayName("Lanzar excepcion cuando no hay pedidos entregados")
    void sinPedidosEntregados() {
        when(propietarioRestaurantePort.obtenerIdRestauranteDelPropietario(1L))
                .thenReturn(Optional.of(5L));
        when(pedidoRepository.findAllByIdRestauranteAndEstado(5L, EstadoPedido.ENTREGADO))
                .thenReturn(List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> consultarEficiencia.consultar(1L));
        assertEquals("No hay pedidos entregados para calcular eficiencia", ex.getMessage());
    }

    @Test
    @DisplayName("Usar diferencia de fechas cuando trazabilidad no tiene duracionEtapaMinutos")
    void fallbackSinDuracion() {
        when(propietarioRestaurantePort.obtenerIdRestauranteDelPropietario(1L))
                .thenReturn(Optional.of(5L));

        LocalDateTime inicio = LocalDateTime.now().minusHours(1);
        LocalDateTime fin = LocalDateTime.now();
        Pedido p1 = new Pedido(10L, 100L, "Cliente", "+57300", 5L,
                EstadoPedido.ENTREGADO, 77L, null, inicio, fin);
        when(pedidoRepository.findAllByIdRestauranteAndEstado(5L, EstadoPedido.ENTREGADO))
                .thenReturn(List.of(p1));

        Trazabilidad t1 = new Trazabilidad("id1", 10L, 100L, 5L, "PENDIENTE", "ENTREGADO",
                inicio.plusMinutes(30), 77L, null);
        when(trazabilidadRepository.findByIdPedidoInOrderByFechaCambioAsc(List.of(10L)))
                .thenReturn(List.of(t1));

        Eficiencia resultado = consultarEficiencia.consultar(1L);

        assertEquals(1, resultado.getPedidos().size());
        assertTrue(resultado.getPedidos().get(0).getTiempoTotalMinutos() >= 0);
    }
}
