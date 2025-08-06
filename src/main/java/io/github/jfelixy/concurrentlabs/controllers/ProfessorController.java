package io.github.jfelixy.concurrentlabs.controllers;


import io.github.jfelixy.concurrentlabs.domain.model.Professor;
import io.github.jfelixy.concurrentlabs.dto.request.ProfessorRequest;
import io.github.jfelixy.concurrentlabs.dto.request.response.ProfessorResponse;
import io.github.jfelixy.concurrentlabs.repository.ProfessorRepository;
import io.github.jfelixy.concurrentlabs.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public ResponseEntity criarProfessor(@RequestBody @Valid ProfessorRequest professorDTO){

        Professor profSafe = professorService.criarProfessor(professorDTO);

        return ResponseEntity.created(URI.create("/professor")).body(toResponse(profSafe));
    }

    private ProfessorResponse toResponse(Professor profSafe){
        return new ProfessorResponse(profSafe.getNome(), profSafe.getEmail(), profSafe.getMatricula(), "CADASTRADO");
    }
}
