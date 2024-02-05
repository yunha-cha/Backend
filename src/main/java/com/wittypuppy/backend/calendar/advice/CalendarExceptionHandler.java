package com.wittypuppy.backend.calendar.advice;

import com.wittypuppy.backend.calendar.exception.RollbackEventException;
import com.wittypuppy.backend.common.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CalendarExceptionHandler {

    @ExceptionHandler(RollbackEventException.class)
    public ResponseEntity<ErrorResponseDTO> handleRollbackEventException(RollbackEventException e) {
        log.info("[CalendarExceptionHandler] >>> handleRollbackEventException >>> ");
        String code = "ERROR_CODE_2001";
        String description = "임시 삭제 상태가 아님";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.BAD_REQUEST);
    }
}
