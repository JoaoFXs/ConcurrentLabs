package io.github.jfelixy.concurrentlabs.controllers;


import io.github.jfelixy.concurrentlabs.dto.request.ProfessorRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ProfessorController {

    @PostMapping
    public ResponseEntity criarProfessor(@RequestBody @Valid ProfessorRequest professor){

        return null;
    }
}
