package io.github.jfelixy.concurrentlabs.controllers;


import io.github.jfelixy.concurrentlabs.domain.model.Professor;
import io.github.jfelixy.concurrentlabs.dto.request.ProfessorRequest;
import io.github.jfelixy.concurrentlabs.dto.request.response.LaboratorioResponse;
import io.github.jfelixy.concurrentlabs.dto.request.response.ProfessorResponse;
import io.github.jfelixy.concurrentlabs.repository.ProfessorRepository;
import io.github.jfelixy.concurrentlabs.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequestMapping("/professor")
@Tag(name = "Professor", description = "Endpoints para gerenciamento de professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping
    @Operation(description = "Cria um novo professor", summary = "Cria um novo professor")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Professor criado com sucesso",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProfessorResponse.class))}),
            @ApiResponse(responseCode = "400,500", description = "Falha na criação do professor", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
            )})
    public ResponseEntity criarProfessor(@RequestBody @Valid ProfessorRequest professorDTO){

        Professor profSafe = professorService.criarProfessor(professorDTO);

        return ResponseEntity.created(URI.create("/professor")).body(toResponse(profSafe));
    }

    private ProfessorResponse toResponse(Professor profSafe){
        return new ProfessorResponse(profSafe.getNome(), profSafe.getEmail(), profSafe.getMatricula(), "CADASTRADO");
    }
}
