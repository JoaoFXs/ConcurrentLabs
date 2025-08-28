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
    public ResponseEntity<List<String>> generateValues() throws Exception {
        Map<String, Supplier<Object>> fields = new HashMap<>();
        fields.put("nome", () -> fakeRequisitions.job().field());
        fields.put("capacidade", () -> fakeRequisitions.number().numberBetween(0,30));
        return generateAndSend(30, fields, "/generate/generateLabs");
    }

    @PostMapping("/generateProfessor")
    public ResponseEntity<List<String>> generateProfessor() throws Exception {
        Map<String, Supplier<Object>> fields = new HashMap<>();
        fields.put("nome", () -> fakeRequisitions.name().firstName());
        fields.put("email", () -> fakeRequisitions.internet().emailAddress());
        fields.put("matricula", () -> fakeRequisitions.number().numberBetween(1000,9999));
        return generateAndSend(5, fields, "/generate/generateProfessor");
    }

    /**
     * Helper method to generate JSONs, send them, and create a response.
     */
    private ResponseEntity<List<String>> generateAndSend(int count, Map<String, Supplier<Object>> fields, String path) throws Exception {
        List<String> jsons = fakeRequisitions.generateJsons(count, fields);

        fakeRequisitions.sendRequisition(jsons);

        return ResponseEntity.created(URI.create(path)).body(jsons);
    }
}
