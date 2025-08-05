package io.github.jfelixy.concurrentlabs.service;


import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import io.github.jfelixy.concurrentlabs.domain.model.enums.StatusReserva;
import io.github.jfelixy.concurrentlabs.exceptions.FalhaProcessamentoLoteException;
import io.github.jfelixy.concurrentlabs.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;

@Service
public class ProcessamentoLoteService {

    private CyclicBarrier barrier;
    private  List<Reserva> reservasPendentes = new CopyOnWriteArrayList<>();

    @Autowired
    private ReservaRepository reservaRepository;

    public ProcessamentoLoteService(CyclicBarrier barrier) {
        this.barrier = new CyclicBarrier(5,this::processarLote);
    }

    private void adicionarReservaLote(Reserva reserva){
        reservasPendentes.add(reserva);
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            throw new FalhaProcessamentoLoteException("Falha no processamento do lot");
        }
    }

    private void processarLote(){
        reservasPendentes.forEach(reserva -> {
            reserva.setStatus(StatusReserva.CONFIRMADA);
            reservaRepository.save(reserva);
            //notificacaoService.enviarConfirmacao(reserva);
        });
        reservasPendentes.clear();
    }
}
