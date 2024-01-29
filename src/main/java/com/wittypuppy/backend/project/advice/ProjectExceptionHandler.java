package com.wittypuppy.backend.project.advice;

import com.wittypuppy.backend.calendar.exception.DeleteEventException;
import com.wittypuppy.backend.common.dto.ErrorResponseDTO;
import com.wittypuppy.backend.project.exception.CreateProjectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProjectExceptionHandler {

    @ExceptionHandler(CreateProjectException.class)
    public ResponseEntity<ErrorResponseDTO> handleCreateProjectException(CreateProjectException e) {
        log.info("[ProjectExceptionHandler] >>> CreateProjectException >>> ");
        String code = "ERROR_CODE_3001";
        String description = "프로젝트 생성 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.BAD_REQUEST);
    }
}
