package com.wittypuppy.backend.common.advice;

import com.wittypuppy.backend.common.dto.ErrorResponseDTO;
import com.wittypuppy.backend.common.exception.DataDeletionException;
import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DataExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataNotFoundException(DataNotFoundException e) {
        log.info("[DataExceptionHandler] >>> handleDataNotFoundException >>> ");
        String code = "ERROR_CODE_001";
        String description = "데이터 조회 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataInsertionException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataInsertionException(DataInsertionException e) {
        log.info("[DataExceptionHandler] >>> handleDataInsertionException >>> ");
        String code = "ERROR_CODE_002";
        String description = "데이터 삽입 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataUpdateException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataUpdateException(DataUpdateException e) {
        log.info("[DataExceptionHandler] >>> handleDataUpdateException >>> ");
        String code = "ERROR_CODE_003";
        String description = "데이터 갱신 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataDeletionException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataDeletionException(DataDeletionException e) {
        String code = "ERROR_CODE_004";
        String description = "데이터 삭제 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String code = "";
        String description = "";
        String detail = "";

        if (e.getBindingResult().hasErrors()) {
            detail = e.getBindingResult().getFieldError().getDefaultMessage();
            String bindResultCode = e.getBindingResult().getFieldError().getCode();

            code = bindResultCode;
        }
        log.info(e.toString());
        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
