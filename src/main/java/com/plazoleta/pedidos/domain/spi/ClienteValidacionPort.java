package com.plazoleta.pedidos.domain.spi;

import com.plazoleta.pedidos.domain.model.value.ClienteInfo;
import java.util.Optional;

public interface ClienteValidacionPort {
    Optional<ClienteInfo> obtenerCliente(Long idCliente);
}