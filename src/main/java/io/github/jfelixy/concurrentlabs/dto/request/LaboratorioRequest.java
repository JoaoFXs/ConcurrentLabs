package io.github.jfelixy.concurrentlabs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação de um Laboratorio")
public record LaboratorioRequest(
        @Schema(description = "Nome do Laboratório", example = "Laboratório 1")
        String nome,
        @Schema(description = "Capacidade do Laboratório", example = "40")
        int capacidade
) {
}
