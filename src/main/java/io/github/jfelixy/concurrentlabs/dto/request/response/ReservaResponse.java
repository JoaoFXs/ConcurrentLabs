package io.github.jfelixy.concurrentlabs.dto.request.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação de uma Reserva Criada")
public record ReservaResponse(
        @Schema(description = "Id da reserva criada")
        Long id,
        @Schema(description = "Nome do Laboratorio reservado", example = "Laboratório 1")
        String laboratorioNome,
        @Schema(description = "Nome do Professor", example = "Professor 1")
        String professorNome,
        @Schema(description = "Data e Hora cadastrada", example = "21/06/2000")
        LocalDateTime dataHora,
        @Schema(description = "Status da reserva", example = "PENDENTE")
        io.github.jfelixy.concurrentlabs.domain.model.enums.StatusReserva status
){}