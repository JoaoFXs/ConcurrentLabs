package io.github.jfelixy.concurrentlabs.service;


import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import io.github.jfelixy.concurrentlabs.dto.request.LaboratorioRequest;
import io.github.jfelixy.concurrentlabs.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Transactional
    public Laboratorio criarLaboratorio(LaboratorioRequest laboratorioRequest){
        return laboratorioRepository.save(new Laboratorio(laboratorioRequest.nome(), laboratorioRequest.capacidade()));
    }
}
