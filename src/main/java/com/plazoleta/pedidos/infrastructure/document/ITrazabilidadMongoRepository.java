package com.plazoleta.pedidos.infrastructure.document;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ITrazabilidadMongoRepository extends MongoRepository<EntidadTrazabilidad, String> {

    List<EntidadTrazabilidad> findByIdPedidoOrderByFechaCambioAsc(Long idPedido);
}
