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

    private CyclicBarrier barrier;//Barreira Ciclica que sincronizará as threads
    private  List<Reserva> reservasPendentes = new CopyOnWriteArrayList<>();//Lista thread-safe para armazenar reservas pendentes

    @Autowired
    private ReservaRepository reservaRepository;//Repositório de reserva
    /**
     * - `5`: Número de threads que devem chegar na barreira antes de prosseguir
     * - `this::processarLote`**: Ação a ser executada quando a barreira for atingida
     *  Quando 5 threads chamam await(), o método processarLote() é disparado automaticamente
     * **/
    public ProcessamentoLoteService(CyclicBarrier barrier) {
        this.barrier = new CyclicBarrier(5,this::processarLote);
    }

    private void adicionarReservaLote(Reserva reserva){
        reservasPendentes.add(reserva);
        try {
            barrier.await();//Aguarda até a barreira ciclica romper em 5
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            throw new FalhaProcessamentoLoteException("Falha no processamento do lot");
        }
    }

    private void processarLote(){
        /**  Processa lotes confirmando-os e salvando no repositorio. Obs: Em breve criar um service update**/
        reservasPendentes.forEach(reserva -> {
            reserva.setStatus(StatusReserva.CONFIRMADA);
            reservaRepository.save(reserva);
            /** Sistema de notificação, será implementado em breve**/
            //notificacaoService.enviarConfirmacao(reserva);
        });
        reservasPendentes.clear();
    }
}
