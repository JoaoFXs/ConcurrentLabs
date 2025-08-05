package io.github.jfelixy.concurrentlabs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequest {
    private Long laboratorioId;
    private Long professorId;
    @Builder.Default
    private LocalDateTime dataHora = LocalDateTime.now();
}
