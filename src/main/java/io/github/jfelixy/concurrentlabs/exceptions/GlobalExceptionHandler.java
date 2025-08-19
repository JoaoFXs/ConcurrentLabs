package io.github.jfelixy.concurrentlabs.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CapacidadeExcedidaException.class)
    public ResponseEntity<ErrorResponse> handleCapacidadeExcedida(CapacidadeExcedidaException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setError(HttpStatus.CONFLICT.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrorCode("LAB_CAPACIDADE_EXCECIDA");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RecursoNotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNotFound ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrorCode("RECURSO_NAO_ENCONTRADO");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
