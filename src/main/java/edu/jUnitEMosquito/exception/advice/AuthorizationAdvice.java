package edu.jUnitEMosquito.exception.advice;

import edu.jUnitEMosquito.exception.authorization.UsuarioNaoPossuiPermissao;
import edu.jUnitEMosquito.exception.dto.GenericExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthorizationAdvice {

    @ExceptionHandler(UsuarioNaoPossuiPermissao.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleUsuarioNaoPossuiPermissao(UsuarioNaoPossuiPermissao ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }
}
