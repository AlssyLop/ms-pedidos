package com.plazoleta.pedidos.infrastructure.config;

import com.plazoleta.pedidos.domain.api.CrearPedidoPort;
import com.plazoleta.pedidos.domain.spi.ClienteValidacionPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.RestauranteValidacionPort;
import com.plazoleta.pedidos.domain.usecase.CrearPedido;
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
}