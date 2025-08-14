package io.github.jfelixy.concurrentlabs.controllers;


import io.github.joaofxs.fake_requisitions.bean.FakeRequisitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@RestController
@RequestMapping("/generate")
public class GenerateJsonsController {
    private  final FakeRequisitions fakeRequisitions;


    @Autowired
    public GenerateJsonsController(FakeRequisitions fakeRequisitions) {
        this.fakeRequisitions = fakeRequisitions;
    }

    @PostMapping("/generateLabs")
    private ResponseEntity<List<String>> generateValues() throws Exception {
        Map<String, Supplier<Object>> campos = new HashMap<>();
        campos.put("nome", () -> fakeRequisitions.job().field());
        campos.put("capacidade", () -> fakeRequisitions.number().numberBetween(0,30));

        List<String> jsons = fakeRequisitions.generateJsons(30, campos);

        fakeRequisitions.sendRequisition(jsons);
        return ResponseEntity.created(URI.create("/lab/generateLabs")).body(jsons);
    }

    @PostMapping("/generateProfessor")
    private ResponseEntity<List<String>> generateProfessor() throws Exception {
        Map<String, Supplier<Object>> campos = new HashMap<>();
        campos.put("nome", () -> fakeRequisitions.name().firstName());
        campos.put("email", () -> fakeRequisitions.internet().emailAddress());
        campos.put("matricula", () -> fakeRequisitions.number().numberBetween(1000,9999));
        List<String> jsons = fakeRequisitions.generateJsons(30, campos);

        fakeRequisitions.sendRequisition(jsons);
        return ResponseEntity.created(URI.create("/lab/generateProfessor")).body(jsons);
    }
}
