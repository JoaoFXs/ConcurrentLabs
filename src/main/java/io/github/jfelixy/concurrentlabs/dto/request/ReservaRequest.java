package io.github.jfelixy.concurrentlabs.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Schema(description = "Representação de uma Reserva Criada")
public record ReservaRequest (
        @Schema(description = "Id do Laboratorio", example = "1")
     Long laboratorioId,
     @Schema(description = "Id do Professor", example = "1")
     Long professorId
){}
