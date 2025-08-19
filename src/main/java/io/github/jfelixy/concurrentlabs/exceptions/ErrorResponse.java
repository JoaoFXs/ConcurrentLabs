package io.github.jfelixy.concurrentlabs.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/** Classe para padronização dos erros **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String errorCode;
    private List<ValidationError> details;

    public ErrorResponse(String errorCode, String message){
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
        this.message = message;
    }

}
