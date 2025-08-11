package io.github.jfelixy.concurrentlabs.dto.request.response;

public record LaboratorioResponse(
        String status,
        String nome,
        int capacidade
) {
}
