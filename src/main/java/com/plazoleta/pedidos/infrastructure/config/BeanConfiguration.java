package com.plazoleta.pedidos.infrastructure.config;

import com.plazoleta.pedidos.domain.api.AsignarPedidoPort;
import com.plazoleta.pedidos.domain.api.CancelarPedidoPort;
import com.plazoleta.pedidos.domain.api.ConsultarTrazabilidadPort;
import com.plazoleta.pedidos.domain.api.CrearPedidoPort;
import com.plazoleta.pedidos.domain.api.EntregarPedidoPort;
import com.plazoleta.pedidos.domain.api.ListarPedidosPorEstadoPort;
import com.plazoleta.pedidos.domain.spi.ClienteValidacionPort;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.NotificacionClientePort;
import com.plazoleta.pedidos.domain.spi.PropietarioRestaurantePort;
import com.plazoleta.pedidos.domain.spi.RestauranteValidacionPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import com.plazoleta.pedidos.domain.usecase.AsignarPedido;
import com.plazoleta.pedidos.domain.usecase.CancelarPedido;
import com.plazoleta.pedidos.domain.usecase.ConsultarEficiencia;
import com.plazoleta.pedidos.domain.usecase.ConsultarTrazabilidad;
import com.plazoleta.pedidos.domain.usecase.CrearPedido;
import com.plazoleta.pedidos.domain.usecase.EntregarPedido;
import com.plazoleta.pedidos.domain.usecase.ListarPedidosPorEstado;
import com.plazoleta.pedidos.domain.usecase.NotificarPedidoListo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CrearPedidoPort crearPedidoPort(PedidoRepositoryPort pedidoRepository,
                                           RestauranteValidacionPort restauranteValidacion,
                                           ClienteValidacionPort clienteValidacion) {
        return new CrearPedido(pedidoRepository, restauranteValidacion, clienteValidacion);
    }

    @Bean
    public ListarPedidosPorEstadoPort listarPedidosPorEstadoPort(PedidoRepositoryPort pedidoRepository) {
        return new ListarPedidosPorEstado(pedidoRepository);
    }

    @Bean
    public AsignarPedidoPort asignarPedidoPort(PedidoRepositoryPort pedidoRepository,
                                                EmpleadoRestaurantePedidosPort empleadoRestaurantePort) {
        return new AsignarPedido(pedidoRepository, empleadoRestaurantePort);
    }

    @Bean
    public CancelarPedidoPort cancelarPedidoPort(PedidoRepositoryPort pedidoRepository) {
        return new CancelarPedido(pedidoRepository);
    }

    @Bean
    public EntregarPedidoPort entregarPedidoPort(PedidoRepositoryPort pedidoRepository,
                                                  EmpleadoRestaurantePedidosPort empleadoRestaurantePort) {
        return new EntregarPedido(pedidoRepository, empleadoRestaurantePort);
    }

    @Bean
    public ConsultarTrazabilidadPort consultarTrazabilidadPort(PedidoRepositoryPort pedidoRepository,
                                                                TrazabilidadRepositoryPort trazabilidadRepository) {
        return new ConsultarTrazabilidad(pedidoRepository, trazabilidadRepository);
    }

    @Bean
    public com.plazoleta.pedidos.domain.api.ConsultarEficienciaPort consultarEficienciaPort(
            PedidoRepositoryPort pedidoRepository,
            PropietarioRestaurantePort propietarioRestaurantePort,
            TrazabilidadRepositoryPort trazabilidadRepository) {
        return new ConsultarEficiencia(pedidoRepository, propietarioRestaurantePort, trazabilidadRepository);
    }

    @Bean
    public com.plazoleta.pedidos.domain.api.NotificarPedidoListoPort notificarPedidoListoPort(
            PedidoRepositoryPort pedidoRepository,
            EmpleadoRestaurantePedidosPort empleadoRestaurantePort,
            NotificacionClientePort notificacionClientePort) {
        return new NotificarPedidoListo(pedidoRepository, empleadoRestaurantePort, notificacionClientePort);
    }
}