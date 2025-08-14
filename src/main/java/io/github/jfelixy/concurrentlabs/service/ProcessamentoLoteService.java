package io.github.jfelixy.concurrentlabs.service;


import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import io.github.jfelixy.concurrentlabs.domain.model.enums.StatusReserva;
import io.github.jfelixy.concurrentlabs.exceptions.FalhaProcessamentoLoteException;
import io.github.jfelixy.concurrentlabs.repository.ReservaRepository;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
@NoArgsConstructor
public class ProcessamentoLoteService {

    @Value("${app.timeout-lote-segundos:30}") // default 30s
    private int timeoutSegundos;

    private CyclicBarrier barrier;//Barreira Ciclica que sincronizará as threads
    private  List<Reserva> reservasPendentes = new CopyOnWriteArrayList<>();//Lista thread-safe para armazenar reservas pendentes
    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private ReservaRepository reservaRepository;//Repositório de reserva
    /**
     * - `5`: Número de threads que devem chegar na barreira antes de prosseguir
     * - `this::processarLote`**: Ação a ser executada quando a barreira for atingida
     *  Quando 5 threads chamam await(), o método processarLote() é disparado automaticamente
     * **/

    @PostConstruct // Inicializa após injeção de dependências
    public void init() {
        this.barrier = new CyclicBarrier(5, this::processarLote);
    }

    public  void adicionarReservaLote(Reserva reserva){
        reservasPendentes.add(reserva);
        try {
            barrier.await(timeoutSegundos, TimeUnit.SECONDS);//Aguarda até a barreira ciclica romper em 5, se demorar mais que 30 segundos para romper, envia as requisições que ja foram feitas
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            throw new FalhaProcessamentoLoteException("Falha no processamento do lot");
        } catch (TimeoutException e) {
            // Timeout: processa o lote atual mesmo incompleto
            processarLote();
        }
    }

    private void processarLote(){
        /**  Processa lotes confirmando-os e salvando no repositorio. Obs: Em breve criar um service update**/
        reservasPendentes.forEach(reserva -> {
            reserva.setStatus(StatusReserva.CONFIRMADA);
            reservaRepository.save(reserva);
            /** Sistema de notificação, será implementado em breve**/
            notificacaoService.enviarConfirmacao(reserva);
        });
        reservasPendentes.clear();
    }
}
