package com.plazoleta.pedidos.application.factory;

import com.plazoleta.pedidos.application.dto.PedidoRequest;
import com.plazoleta.pedidos.application.dto.PlatoPedidoRequest;
import com.plazoleta.pedidos.application.exception.ValidacionException;
import com.plazoleta.pedidos.domain.model.DetallePedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PedidoFactory {

    public Pedido toDomain(PedidoRequest request, Long idCliente, String nombreCliente, String celular) {
        validarRequest(request);

        List<DetallePedido> detalles = new ArrayList<>();
        for (PlatoPedidoRequest platoReq : request.getPlatos()) {
            detalles.add(new DetallePedido(null, null, platoReq.getIdPlato(), null, platoReq.getCantidad()));
        }

        Pedido pedido = new Pedido(null, idCliente, nombreCliente, celular,
                request.getIdRestaurante(), null, null, null,
                LocalDateTime.now(), LocalDateTime.now());
        pedido.setDetalles(detalles);
        return pedido;
    }

    private void validarRequest(PedidoRequest request) {
        if (request.getIdRestaurante() == null) {
            throw new ValidacionException("idRestaurante", "El restaurante es obligatorio");
        }
        if (request.getPlatos() == null || request.getPlatos().isEmpty()) {
            throw new ValidacionException("platos", "Debe incluir al menos un plato");
        }
        for (int i = 0; i < request.getPlatos().size(); i++) {
            PlatoPedidoRequest plato = request.getPlatos().get(i);
            if (plato.getIdPlato() == null) {
                throw new ValidacionException("platos[" + i + "].idPlato", "El plato es obligatorio");
            }
            if (plato.getCantidad() == null || plato.getCantidad() < 1) {
                throw new ValidacionException("platos[" + i + "].cantidad", "La cantidad debe ser mayor a 0");
            }
        }
    }
}
