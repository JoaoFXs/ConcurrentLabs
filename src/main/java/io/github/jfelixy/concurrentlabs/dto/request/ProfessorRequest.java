package io.github.jfelixy.concurrentlabs.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação de um Professsor")
public record ProfessorRequest(
        @Schema(description = "Nome do Professor", example = "João Victor")
        String nome,
        @Schema(description = "Email do Professor", example = "teste@gmail.com")
        String email,
        @Schema(description = "Matricula do Professor", example = "2102157")
        String matricula
) {
}
