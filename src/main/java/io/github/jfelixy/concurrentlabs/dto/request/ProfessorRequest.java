package io.github.jfelixy.concurrentlabs.dto.request;

public record ProfessorRequest(
        String nome,
        String email,
        String matricula
) {
}
