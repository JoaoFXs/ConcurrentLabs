package io.github.jfelixy.concurrentlabs.controllers;

import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import io.github.jfelixy.concurrentlabs.repository.ReservaRepository;
import io.github.jfelixy.concurrentlabs.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity criarReserva(@RequestBody @Valid ReservaRequest request){

        Reserva reserva = reservaService.criarReserva( request.laboratorioId(),
                request.professorId(),
                request.dataHora());

        return ResponseEntity.created(URI.create("/reservas/" + reserva.getId()));
    }
}
