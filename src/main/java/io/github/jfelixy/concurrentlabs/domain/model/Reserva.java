package io.github.jfelixy.concurrentlabs.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Laboratorio laboratorio;

    @ManyToOne
    private Professor professor;

    private LocalDateTime dataHora;

    private StatusReserva status;// PENDENTE, CONFIRMADO, CANCELADA

}
