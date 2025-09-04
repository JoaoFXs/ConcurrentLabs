package io.github.jfelixy.concurrentlabs.dto.request.response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta para criação de professores")
public record ProfessorResponse(
        @Schema(description = "Nome do Professor criado", example = "João Victor")
        String nome,
        @Schema(description = "Email do professor criado", example = "teste@gmail.com")
        String email,
        @Schema(description = "Matricula do professor criado", example = "2102157")
        String matricula,
        @Schema(description = "Status do professor criado", example = "CADASTRADO")
        String status) {
}
