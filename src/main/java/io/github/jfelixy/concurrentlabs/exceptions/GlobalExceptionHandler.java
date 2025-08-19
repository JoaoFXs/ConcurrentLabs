package io.github.jfelixy.concurrentlabs.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(FalhaProcessamentoLoteException.class)
    public ResponseEntity<ErrorResponse> handleFalhaProcessamentoLote(FalhaProcessamentoLoteException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrorCode("FALHA_PROCESSAMENTO_LOTE");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ValidationError> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::mapToValidationError)
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrorCode("DADOS_INVALIDOS");
        error.setDetails(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        List<ValidationError> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String fieldName = violation.getPropertyPath().toString();
                    return new ValidationError(
                            fieldName,
                            violation.getMessage(),
                            (String) violation.getInvalidValue()
                    );
                })
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrorCode("DADOS_INVALIDOS");
        error.setDetails(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMessage("Erro interno no servidor " + ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setErrorCode("ERRO_INTERNO");

        // Log do erro completo para debugging
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    private ValidationError mapToValidationError(FieldError fieldError) {
        return new ValidationError(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                (String) fieldError.getRejectedValue());
    }
}
