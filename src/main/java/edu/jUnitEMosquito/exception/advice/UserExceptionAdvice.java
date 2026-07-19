package edu.jUnitEMosquito.exception.advice;

import edu.jUnitEMosquito.exception.dto.GenericExceptionResponseDTO;
import edu.jUnitEMosquito.exception.usuario.DadoInvalidoException;
import edu.jUnitEMosquito.exception.usuario.UsuarioNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionAdvice {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DadoInvalidoException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleDadoInvalido(DadoInvalidoException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
