package io.github.jfelixy.concurrentlabs.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Service
public class ReservaService {

    /** Armazena semáforos únicos para cada laboratório
     *  Chave: ID do Lab
     *  Valor: Semáforo com permissões = capacidade do laborátorio
     *  ConcurrentHashMap para thread-safe
     * **/
    ConcurrentHashMap<Long, Semaphore> semaphores = new ConcurrentHashMap<>();


}
