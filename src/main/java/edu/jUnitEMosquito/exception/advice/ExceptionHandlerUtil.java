package edu.jUnitEMosquito.exception.advice;

import edu.jUnitEMosquito.exception.dto.GenericExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionHandlerUtil {

    public static ResponseEntity<GenericExceptionResponseDTO> buildResponse(HttpStatus status, String message) {
        GenericExceptionResponseDTO response = new GenericExceptionResponseDTO(status, message);
        return ResponseEntity.status(status).body(response);
    }
}
