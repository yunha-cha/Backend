package com.wittypuppy.backend.calendar.advice;

import com.wittypuppy.backend.calendar.exception.CreateEventException;
import com.wittypuppy.backend.common.dto.ErrorResponseDTO;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CalendarExceptionHandler {

    @ExceptionHandler(CreateEventException.class)
    public ResponseEntity<ErrorResponseDTO> handleCreateEventException(CreateEventException e) {
        log.info("[CalendarExceptionHandler] >>> handleCreateEventException >>> ");
        String code = "ERROR_CODE_2001";
        String description = "데이터 조회 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.NOT_FOUND);
    }
}
