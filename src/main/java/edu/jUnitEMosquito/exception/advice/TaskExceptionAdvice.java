package edu.jUnitEMosquito.exception.advice;

import edu.jUnitEMosquito.exception.dto.GenericExceptionResponseDTO;
import edu.jUnitEMosquito.exception.task.GrupoNaoPossuiTasksException;
import edu.jUnitEMosquito.exception.task.TaskNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TaskExceptionAdvice {

    @ExceptionHandler(TaskNaoEncontradaException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleTaskNotFound(TaskNaoEncontradaException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(GrupoNaoPossuiTasksException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleGrupoSemTasks(GrupoNaoPossuiTasksException ex){
        return ExceptionHandlerUtil.buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
