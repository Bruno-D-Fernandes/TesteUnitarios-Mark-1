package edu.jUnitEMosquito.exception.advice;

import edu.jUnitEMosquito.exception.dto.GenericExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleRuntime(RuntimeException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
