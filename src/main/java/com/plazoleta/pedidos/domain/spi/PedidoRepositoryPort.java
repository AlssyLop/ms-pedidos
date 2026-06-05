package com.plazoleta.pedidos.domain.spi;

import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepositoryPort {

    Pedido save(Pedido pedido);

    Optional<Pedido> findById(Long id);

    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<EstadoPedido> estados);

    Page<Pedido> findByIdRestaurante(Long idRestaurante, Pageable pageable);

    Page<Pedido> findByIdRestauranteAndEstado(Long idRestaurante, EstadoPedido estado, Pageable pageable);
}
