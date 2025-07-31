package io.github.jfelixy.concurrentlabs.service;


import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import io.github.jfelixy.concurrentlabs.domain.model.Professor;
import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import io.github.jfelixy.concurrentlabs.domain.model.enums.StatusReserva;
import io.github.jfelixy.concurrentlabs.exceptions.CapacidadeExcedidaException;
import io.github.jfelixy.concurrentlabs.exceptions.LabNotFound;
import io.github.jfelixy.concurrentlabs.exceptions.ProfNotFound;
import io.github.jfelixy.concurrentlabs.repository.LaboratorioRepository;
import io.github.jfelixy.concurrentlabs.repository.ProfessorRepository;
import io.github.jfelixy.concurrentlabs.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Service
public class ReservaService {

    /**
     * Armazena semáforos únicos para cada laboratório
     * Chave: ID do Lab
     * Valor: Semáforo com permissões = capacidade do laborátorio
     * ConcurrentHashMap para thread-safe
     **/
    ConcurrentHashMap<Long, Semaphore> semaphores = new ConcurrentHashMap<>();

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private LaboratorioRepository labRepository;

    @Autowired
    private ProfessorRepository profRepository;

    @Transactional
    private Reserva criarReserva(Long laboratorioId, Long professorId, LocalDateTime dataHora) {
        /** Procura o laboratorio pelo id, se não achar, entra em exception**/
        Laboratorio lab = labRepository.findById(laboratorioId)
                .orElseThrow(() -> new LabNotFound("Laboratorio não encontrado pelo id " + laboratorioId));

        /** Operação atômica que, se existir semáforo para o laborátorio, retorna o existente. Se não, cria novo semáforo com capcidade = numero computadores**/
        Semaphore semaphore = semaphores.computeIfAbsent(laboratorioId,
                id -> new Semaphore(lab.getCapacidadeComputadores())
        );
        Professor prof = profRepository.findById(professorId).orElseThrow(() -> new ProfNotFound("Professor não encontrado pelo id " + professorId));

        if (semaphore.tryAcquire()) {
            Reserva novaReserva = new Reserva();
            novaReserva.setDataHora(dataHora);
            novaReserva.setLaboratorio(lab);
            novaReserva.setProfessor(prof);
            novaReserva.setStatus(StatusReserva.PENDENTE);
            return reservaRepository.save(novaReserva);
        } else {
            throw new CapacidadeExcedidaException("Capacidade de" + lab.getCapacidadeComputadores() + "do laborátorio excedida, não há computadores disponiveis. Tente novamente mais tarde!");
        }
    }


}
