package io.github.jfelixy.concurrentlabs.controllers;

import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import io.github.jfelixy.concurrentlabs.dto.request.LaboratorioRequest;
import io.github.jfelixy.concurrentlabs.dto.request.response.LaboratorioResponse;
import io.github.jfelixy.concurrentlabs.service.LaboratorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/lab")
@Tag(name = "Laboratório", description = "Endpoints para gerenciamento de laboratórios")
public class LaboratorioController {

    @Autowired
    private LaboratorioService labService;



    @PostMapping
    @Operation(description = "Cria um novo laboratório", summary = "Cria um novo laboratório")
    @ApiResponses( value = {
                    @ApiResponse(responseCode = "201", description = "Laboratório criado com sucesso",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LaboratorioResponse.class))}),
                    @ApiResponse(responseCode = "400,500", description = "Falha na criação do laboratório", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}
    )})
    private ResponseEntity<LaboratorioResponse> criarLaboratorio(@RequestBody LaboratorioRequest laboratorioRequest){
        
        Laboratorio lab = labService.criarLaboratorio(laboratorioRequest);
        return ResponseEntity.created(URI.create("/lab")).body(toResponse(lab));

    }

    private LaboratorioResponse toResponse(Laboratorio lab){
        return new LaboratorioResponse("CRIADO", lab.getNome(), lab.getCapacidadeComputadores());
    }

}
