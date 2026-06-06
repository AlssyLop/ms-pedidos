package com.plazoleta.pedidos.infrastructure.endpoint;

import com.plazoleta.pedidos.application.dto.EficienciaResponse;
import com.plazoleta.pedidos.application.handle.ConsultarEficienciaHandle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurante")
@Tag(name = "Eficiencia", description = "Consulta de eficiencia de pedidos")
public class EficienciaController {

    private final ConsultarEficienciaHandle consultarEficienciaHandle;

    public EficienciaController(ConsultarEficienciaHandle consultarEficienciaHandle) {
        this.consultarEficienciaHandle = consultarEficienciaHandle;
    }

    @GetMapping("/eficiencia")
    @PreAuthorize("hasRole('PROPIETARIO')")
    @Operation(summary = "Consultar eficiencia de pedidos",
            description = "El propietario consulta los tiempos de entrega de los pedidos de su restaurante y el ranking de empleados.")
    @ApiResponse(responseCode = "200", description = "Eficiencia calculada exitosamente")
    @ApiResponse(responseCode = "404", description = "No se encontro restaurante o no hay pedidos entregados")
    public ResponseEntity<EficienciaResponse> consultarEficiencia(Authentication authentication) {
        EficienciaResponse response = consultarEficienciaHandle.consultar(authentication);
        return ResponseEntity.ok(response);
    }
}
