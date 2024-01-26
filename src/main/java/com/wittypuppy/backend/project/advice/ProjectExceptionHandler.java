package com.wittypuppy.backend.project.advice;

import com.wittypuppy.backend.common.dto.ErrorResponseDTO;
import com.wittypuppy.backend.project.exception.ProjectIsLockedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProjectExceptionHandler {

    @ExceptionHandler(ProjectIsLockedException.class)
    public ResponseEntity<ErrorResponseDTO> handleProjectIsLockedException(ProjectIsLockedException e) {
        log.info("[ProjectExceptionHandler] >>> handleProjectIsLockedException >>> ");
        String code = "ERROR_CODE_2001";
        String description = "프로젝트 잠금 상태";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.NOT_FOUND);
    }
}
