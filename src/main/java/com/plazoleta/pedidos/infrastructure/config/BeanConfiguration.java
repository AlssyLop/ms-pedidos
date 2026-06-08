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
                                           ClienteValidacionPort clienteValidacion,
                                           TrazabilidadRepositoryPort trazabilidadRepository) {
        return new CrearPedido(pedidoRepository, restauranteValidacion, clienteValidacion, trazabilidadRepository);
    }

    @Bean
    public ListarPedidosPorEstadoPort listarPedidosPorEstadoPort(PedidoRepositoryPort pedidoRepository) {
        return new ListarPedidosPorEstado(pedidoRepository);
    }

    @Bean
    public AsignarPedidoPort asignarPedidoPort(PedidoRepositoryPort pedidoRepository,
                                                 EmpleadoRestaurantePedidosPort empleadoRestaurantePort,
                                                 TrazabilidadRepositoryPort trazabilidadRepository) {
        return new AsignarPedido(pedidoRepository, empleadoRestaurantePort, trazabilidadRepository);
    }

    @Bean
    public CancelarPedidoPort cancelarPedidoPort(PedidoRepositoryPort pedidoRepository,
                                                  TrazabilidadRepositoryPort trazabilidadRepository) {
        return new CancelarPedido(pedidoRepository, trazabilidadRepository);
    }

    @Bean
    public EntregarPedidoPort entregarPedidoPort(PedidoRepositoryPort pedidoRepository,
                                                  EmpleadoRestaurantePedidosPort empleadoRestaurantePort,
                                                  TrazabilidadRepositoryPort trazabilidadRepository) {
        return new EntregarPedido(pedidoRepository, empleadoRestaurantePort, trazabilidadRepository);
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
            NotificacionClientePort notificacionClientePort,
            TrazabilidadRepositoryPort trazabilidadRepository) {
        return new NotificarPedidoListo(pedidoRepository, empleadoRestaurantePort, notificacionClientePort, trazabilidadRepository);
    }
}