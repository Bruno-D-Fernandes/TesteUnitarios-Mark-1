package edu.jUnitEMosquito.exception.advice;

import edu.jUnitEMosquito.exception.dto.GenericExceptionResponseDTO;
import edu.jUnitEMosquito.exception.group.GrupoNaoEncontrado;
import edu.jUnitEMosquito.exception.group.NomeDoGrupoInvalido;
import edu.jUnitEMosquito.exception.group.UsuarioJaPossuiGrupoComEsseNomeException;
import edu.jUnitEMosquito.exception.group.UsuarioNaoParticipaDoGrupo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GroupExceptionAdvice {

    @ExceptionHandler(UsuarioJaPossuiGrupoComEsseNomeException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleUsuarioJaPossuiGrupo(UsuarioJaPossuiGrupoComEsseNomeException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NomeDoGrupoInvalido.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleNomeDoGrupoInvalido(NomeDoGrupoInvalido ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(GrupoNaoEncontrado.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleGrupoNaoEncontrado(GrupoNaoEncontrado ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoParticipaDoGrupo.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleUsuarioNaoParticipa(UsuarioNaoParticipaDoGrupo ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

}
