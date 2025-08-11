package io.github.jfelixy.concurrentlabs.controllers;

import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import io.github.jfelixy.concurrentlabs.dto.request.LaboratorioRequest;
import io.github.jfelixy.concurrentlabs.dto.request.response.LaboratorioResponse;
import io.github.jfelixy.concurrentlabs.service.LaboratorioService;
import io.github.joaofxs.fake_requisitions.bean.FakeRequisitions;
import io.github.joaofxs.fake_requisitions.config.FakeRequisitionsAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lab")
public class LaboratorioController {

    @Autowired
    private FakeRequisitions fakeRequisitions;


    @Autowired
    private LaboratorioService labService;



    @PostMapping
    private ResponseEntity<Laboratorio> criarLaboratorio(@RequestBody LaboratorioRequest laboratorioRequest){

        return ResponseEntity.created(URI.create("/lab")).body(labService.criarLaboratorio(laboratorioRequest));

    }
    @PostMapping("/generateLabs")
    private ResponseEntity<List<String>> generateValues() throws Exception {
        Map<String,String> campos = Map.of();
        campos.put("nome", fakeRequisitions.educator().course());
        campos.put("capacidade", String.valueOf(fakeRequisitions.number().numberBetween(0,30)));

        List<String> jsons = fakeRequisitions.generateJsons(3, campos);
        fakeRequisitions.sendRequisition(jsons);
        return ResponseEntity.created(null).body(jsons);
    }

    private LaboratorioResponse toResponse(Laboratorio lab){
        return new LaboratorioResponse("CRIADO", lab.getNome(), lab.getCapacidadeComputadores());
    }

}
