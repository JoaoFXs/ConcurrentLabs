package io.github.jfelixy.concurrentlabs.domain.model;

import io.github.joaofxs.fake_requisitions.bean.FakeRequisitions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laboratorio {
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private int capacidadeComputadores; // Máx. computadores disponíveis

    @OneToMany(mappedBy = "laboratorio")
    private List<Reserva> reservas;

    public Laboratorio(String nome, int capacidadeComputadores) {
        this.nome = nome;
        this.capacidadeComputadores = capacidadeComputadores;
    }

}
