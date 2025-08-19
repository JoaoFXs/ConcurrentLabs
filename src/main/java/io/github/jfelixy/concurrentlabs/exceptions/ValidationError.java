package io.github.jfelixy.concurrentlabs.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Utilizado no mapeamento do ErrorResponse **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    private String field;
    private String message;
    private String rejectedValue;
}
