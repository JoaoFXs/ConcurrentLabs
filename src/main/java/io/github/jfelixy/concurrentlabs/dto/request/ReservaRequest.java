package io.github.jfelixy.concurrentlabs.dto.request;


import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


public record ReservaRequest (
     Long reservaId,
     Long laboratorioId,
     Long professorId,
     LocalDateTime dataHora
){}
