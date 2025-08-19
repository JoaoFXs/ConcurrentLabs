package io.github.jfelixy.concurrentlabs.service;


import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import io.github.jfelixy.concurrentlabs.domain.model.enums.StatusReserva;
import io.github.jfelixy.concurrentlabs.exceptions.FalhaProcessamentoLoteException;
import io.github.jfelixy.concurrentlabs.exceptions.RecursoNotFound;
import io.github.jfelixy.concurrentlabs.repository.ReservaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ProcessamentoLoteService {
    /**  fila bloqueante, mais eficiente para operações concorrentes **/
    private final BlockingQueue<Reserva> reservasPendentes = new LinkedBlockingQueue<>();
    /** Agendador para processamento periódico **/
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    /** Tamanho do lote (5) e intervalo de processamento (30 segundos) **/
    private static final int TAMANHO_LOTE = 5;
    private static final int TIMEOUT_LOTE = 30; // segundos

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    /**Agenda a execução periódica do processamento de lotes a cada 30 segundos
     Garante que o processamento ocorrerá mesmo com poucas reservas (resolvendo o problema de espera infinita) **/
    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(this::processarLote, TIMEOUT_LOTE, TIMEOUT_LOTE, TimeUnit.SECONDS);
    }
    /** Adiciona reservas à fila; Dispara processamento imediato se o tamanho do lote for atingido (5 reservas); Combina processamento por tamanho e por tempo **/
    public void adicionarReservaLote(Reserva reserva) {
        reservasPendentes.add(reserva);
        if (reservasPendentes.size() >= TAMANHO_LOTE) {
            processarLote();
        }
    }
    /** drainTo(): Transfere reservas da fila para uma lista local de forma atômica
        Processa apenas se houver reservas pendentes
        Processa cada reserva individualmente em transações separada **/
    private void processarLote() {
        List<Reserva> lote = new ArrayList<>();
        reservasPendentes.drainTo(lote, TAMANHO_LOTE);

        if (lote.isEmpty()) return;

        // Processa em transações separadas
        lote.forEach(this::processarReservaIndividual);
    }

    /**REQUIRES_NEW: Cria nova transação para cada reserva, isolando falhas

     Recarrega a entidade: Evita conflitos de versão otimista

     Tratamento individual de erros: Uma falha não afeta as demais reservas

     Confirmação e notificação: Atualiza status e envia notificação **/
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void processarReservaIndividual(Reserva reserva) {
        try {
            // Recarrega a reserva para evitar conflitos de versão
            Reserva reservaAtualizada = reservaRepository.findById(reserva.getId())
                    .orElseThrow(() -> new RecursoNotFound("Reserva não encontrada"));

            reservaAtualizada.setStatus(StatusReserva.CONFIRMADA);
            reservaRepository.save(reservaAtualizada);
            notificacaoService.enviarConfirmacao(reservaAtualizada);
        } catch (FalhaProcessamentoLoteException e) {
            System.err.println("Erro processando reserva " + reserva.getId() + ": " + e.getMessage());
        }
    }
    /** Desliga o scheduler quando a aplicação é encerrada, evitando "threads zumbis" **/
    @PreDestroy
    public void cleanup() {
        scheduler.shutdown();
    }
}
