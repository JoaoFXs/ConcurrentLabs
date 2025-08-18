package io.github.jfelixy.concurrentlabs.service;

import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import io.github.jfelixy.concurrentlabs.domain.model.Professor;
import io.github.jfelixy.concurrentlabs.exceptions.CapacidadeExcedidaException;
import io.github.jfelixy.concurrentlabs.repository.LaboratorioRepository;
import io.github.jfelixy.concurrentlabs.repository.ProfessorRepository;
import io.github.jfelixy.concurrentlabs.repository.ReservaRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Teste unitários para classe ReservaService **/
@SpringBootTest
@ActiveProfiles("test")/** Referência o perfil de teste com banco em memória h2 **/
public class ReservaServiceConcurrencyTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @BeforeEach
    void setup(){
        reservaRepository.deleteAll();
        professorRepository.deleteAll();
        laboratorioRepository.deleteAll();

        //Cria labs para testes
        Laboratorio lab = new Laboratorio();
        lab.setNome("Lab Informatica");
        lab.setCapacidadeComputadores(10);
        laboratorioRepository.save(lab);
        //Cria professor para testes
        Professor prof = new Professor();
        prof.setNome("João Victor");
        prof.setEmail("jovibfel@gmail.com");
        professorRepository.save(prof);

    }

    @Test
    void testReservaServiceConcorrentes() throws InterruptedException{
        Laboratorio lab = laboratorioRepository.findAll().get(0);
        Professor prof = professorRepository.findAll().get(0);
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        LocalDateTime baseTime = LocalDateTime.now().plusHours(1);

        for (int i = 0; i < threadCount; i++) {
            final int offset = i;
            executor.execute(() -> {
                try {
                    reservaService.criarReserva(lab.getId(), prof.getId(), baseTime.plusMinutes(offset));
                    successCount.incrementAndGet();
                } catch (CapacidadeExcedidaException e) {
                    failureCount.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("Erro inesperado: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(2, TimeUnit.MINUTES);

        long reservasCriadas = reservaRepository.count();
        System.out.println("Reservas bem-sucedidas: " + successCount.get());
        System.out.println("Reservas falhas: " + failureCount.get());
        System.out.println("Total no banco: " + reservasCriadas);

        assertEquals(10, successCount.get());
        assertEquals(10, failureCount.get());
        assertEquals(10, reservasCriadas);
    }

}
