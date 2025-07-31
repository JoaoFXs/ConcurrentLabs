package io.github.jfelixy.concurrentlabs.domain.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private int capacidadeComputadores; // Máx. computadores disponíveis

    @OneToMany(mappedBy = "laboratorio")
    private List<Reserva> reservas;


}
