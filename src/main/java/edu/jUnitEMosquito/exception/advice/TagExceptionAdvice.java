package edu.jUnitEMosquito.exception.advice;

import edu.jUnitEMosquito.exception.dto.GenericExceptionResponseDTO;
import edu.jUnitEMosquito.exception.tag.GrupoNaoPossuiTagsException;
import edu.jUnitEMosquito.exception.tag.TagNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TagExceptionAdvice {

    @ExceptionHandler(TagNaoEncontradaException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleTagNotFound(TagNaoEncontradaException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(GrupoNaoPossuiTagsException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleGrupoSemTags(GrupoNaoPossuiTagsException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
