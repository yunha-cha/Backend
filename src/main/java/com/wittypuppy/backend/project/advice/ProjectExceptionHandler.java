package com.wittypuppy.backend.project.advice;

import com.wittypuppy.backend.common.dto.ErrorResponseDTO;
import com.wittypuppy.backend.project.exception.InvalidProjectMemberException;
import com.wittypuppy.backend.project.exception.NotProjectManagerException;
import com.wittypuppy.backend.project.exception.ProjectKickedException;
import com.wittypuppy.backend.project.exception.ProjectManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProjectExceptionHandler {

    @ExceptionHandler(InvalidProjectMemberException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidProjectMemberException(InvalidProjectMemberException e) {
        log.info("[ProjectExceptionHandler] >>> invalidProjectMemberException >>> ");
        String code = "ERROR_CODE_6001";
        String description = "허가되지 않은 접근";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotProjectManagerException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotProjectManagerException(NotProjectManagerException e) {
        log.info("[ProjectExceptionHandler] >>> NotProjectManagerException >>> ");
        String code = "ERROR_CODE_6002";
        String description = "관리자 권한이 아닙니다.";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectManagerException.class)
    public ResponseEntity<ErrorResponseDTO> handleProjectManagerException(ProjectManagerException e) {
        log.info("[ProjectExceptionHandler] >>> NotProjectManagerException >>> ");
        String code = "ERROR_CODE_6003";
        String description = "관리자 권한입니다.";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectKickedException.class)
    public ResponseEntity<ErrorResponseDTO> handleProjectKickedException(ProjectKickedException e) {
        log.info("[ProjectExceptionHandler] >>> NotProjectManagerException >>> ");
        String code = "ERROR_CODE_6004";
        String description = "프로젝트에서 내보내기 중에 생긴 오류";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.BAD_REQUEST);
    }
}
