package io.github.jfelixy.concurrentlabs.controllers;

import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import io.github.jfelixy.concurrentlabs.dto.request.ReservaRequest;
import io.github.jfelixy.concurrentlabs.dto.request.response.ReservaResponse;
import io.github.jfelixy.concurrentlabs.repository.ReservaRepository;
import io.github.jfelixy.concurrentlabs.service.ReservaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    /** Reserva serv **/
    @Autowired
    private ReservaService reservaService;

    private static final Logger logs = LoggerFactory.getLogger(ReservaController.class);
    /**
     * Endpoint responsável por criar uma nova reserva.
     *
     * Recebe um objeto {@link ReservaRequest} com os dados necessários para a criação de uma reserva,
     * como o ID do laboratório e o ID do professor. A data e hora da reserva são automaticamente
     * definidas com base na hora atual do sistema.
     *
     * Após a criação, retorna uma resposta HTTP 201 (Created), contendo:
     * - o header `Location` com a URI da nova reserva criada,
     * - o corpo da resposta com os dados da reserva encapsulados em um {@link ReservaResponse}.
     *
     * @param request objeto contendo os dados para criação da reserva (validação aplicada via Bean Validation).
     * @return {@link ResponseEntity} com status 201 e corpo contendo os dados da reserva criada.
     */

    @PostMapping
    public ResponseEntity criarReserva(@RequestBody @Valid ReservaRequest request){
        Reserva reserva = reservaService.criarReserva(request.laboratorioId(),
                                                      request.professorId(),
                                                      LocalDateTime.now());
        return ResponseEntity.created(URI.create("/reservas/" + reserva.getId())).body(toResponse(reserva));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletarReserva(@PathVariable Long id){
        reservaService.deleteReservaById(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * Converte uma entidade {@link Reserva} para um objeto {@link ReservaResponse}.
     * Extrai os dados relevantes da entidade de reserva, como:
     * - ID da reserva,
     * - nome do laboratório,
     * - nome do professor,
     * - data e hora atual (representando o momento da resposta),
     * - status da reserva.
     * @param reserva entidade {@link Reserva} a ser convertida.
     * @return instância de {@link ReservaResponse} com os dados mapeados.
     */

    public ReservaResponse toResponse(Reserva reserva){
        return new ReservaResponse(
                reserva.getId(),
                reserva.getLaboratorio().getNome(),
                reserva.getProfessor().getNome(),
                reserva.getDataHora(),
                reserva.getStatus()
        );
    }
}
