package io.github.jfelixy.concurrentlabs.controllers;

import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import io.github.jfelixy.concurrentlabs.dto.request.LaboratorioRequest;
import io.github.jfelixy.concurrentlabs.dto.request.response.LaboratorioResponse;
import io.github.jfelixy.concurrentlabs.service.LaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/lab")
public class LaboratorioController {

    @Autowired
    private LaboratorioService labService;
    @PostMapping
    private ResponseEntity<LaboratorioResponse> criarLaboratorio(@RequestBody LaboratorioRequest laboratorioRequest){
        
        Laboratorio lab = labService.criarLaboratorio(laboratorioRequest);
        return ResponseEntity.created(URI.create("/lab")).body(toResponse(lab));

    }

    private LaboratorioResponse toResponse(Laboratorio lab){
        return new LaboratorioResponse("CRIADO", lab.getNome(), lab.getCapacidadeComputadores());
    }

}
