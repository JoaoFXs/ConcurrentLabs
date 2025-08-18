package io.github.jfelixy.concurrentlabs.domain.model;

import io.github.jfelixy.concurrentlabs.domain.model.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    @Version
    private Long version;
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
