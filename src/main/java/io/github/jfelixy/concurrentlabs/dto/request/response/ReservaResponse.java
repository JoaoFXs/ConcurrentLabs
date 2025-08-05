package io.github.jfelixy.concurrentlabs.dto.request.response;

import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        String laboratorioNome,
        String professorNome,
        LocalDateTime dataHora,
        io.github.jfelixy.concurrentlabs.domain.model.enums.StatusReserva status
){}