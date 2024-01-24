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
        String code = "ERROR_CODE_1001";
        String description = "데이터 조회 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataInsertionException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataInsertionException(DataInsertionException e) {
        log.info("[DataExceptionHandler] >>> handleDataInsertionException >>> ");
        String code = "ERROR_CODE_1002";
        String description = "데이터 삽입 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataUpdateException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataUpdateException(DataUpdateException e) {
        log.info("[DataExceptionHandler] >>> handleDataUpdateException >>> ");
        String code = "ERROR_CODE_1003";
        String description = "데이터 갱신 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataDeletionException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataDeletionException(DataDeletionException e) {
        String code = "ERROR_CODE_1004";
        String description = "데이터 삭제 실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String code = "ERROR_CODE_0000";
        String description = "알 수 없는 오류가 발생했습니다.";
        String detail = "예상하지 못한 오류가 발생했습니다. 다시 시도해 주세요.";

        if (e.getBindingResult().hasErrors()) {
            detail = e.getBindingResult().getFieldError().getDefaultMessage(); // e.getMessage()

            String bindResultCode = e.getBindingResult().getFieldError().getCode();

            switch (bindResultCode) {
                case "NotNull":
                    code = "ERROR_CODE_0001";
                    description = "빈 값을 입력했습니다. 다시 시도해 주세요.";
                    break;
                case "NotEmpty":
                    code = "ERROR_CODE_0002";
                    description = "빈 값을 입력했습니다. 다시 시도해 주세요.";
                    break;
                case "NotBlank":
                    code = "ERROR_CODE_0003";
                    description = "빈 값이나 공백을 입력했습니다. 다시 시도해 주세요.";
                    break;
                case "Email":
                    code = "ERROR_CODE_0004";
                    description = "이메일 형식이 아닙니다. 다시 시도해 주세요.";
                    break;
                case "Pattern":
                    code = "ERROR_CODE_0005";
                    description = "조건에 맞게 다시 시도해 주세요.";
                    break;
                case "Size":
                    code = "ERROR_CODE_0006";
                    description = "길이가 기준치보다 짧거나 깁니다. 다시 시도해 주세요.";
                    break;
                case "Max":
                    code = "ERROR_CODE_0007";
                    description = "기준치보다 크거나 같은 숫자 입니다. 다시 시도해 주세요.";
                    break;
                case "Min":
                    code = "ERROR_CODE_0008";
                    description = "기준치보다 작거나 같은 숫자 입니다. 다시 시도해 주세요.";
                    break;
                case "Positive":
                    code = "ERROR_CODE_0009";
                    description = "음수나 0을 입력할 수 없습니다. 다시 시도해 주세요.";
                    break;
                case "PositiveOrZero":
                    code = "ERROR_CODE_0010";
                    description = "0보다 작은 음수를 입력할 수 없습니다. 다시 시도해 주세요.";
                    break;
                case "Negative":
                    code = "ERROR_CODE_0011";
                    description = "양수나 0을 입력할 수 없습니다. 다시 시도해 주세요.";
                    break;
                case "NegativeOrZero":
                    code = "ERROR_CODE_0012";
                    description = "0보다 큰 양수를 입력할 수 없습니다. 다시 시도해 주세요.";
                    break;
                case "Future":
                    code = "ERROR_CODE_0013";
                    description = "현재날짜보다 미래의 시간으로 입력해야 합니다. 다시 시도해 주세요.";
                    break;
                case "Past":
                    code = "ERROR_CODE_0014";
                    description = "현재날짜보다 과거의 시간으로 입력해야 합니다. 다시 시도해 주세요.";
                    break;
                case "AssertFalse":
                    code = "ERROR_CODE_0015";
                    description = "false값만 들어올 수 있습니다. 다시 시도해 주세요.";
                    break;
                case "AssertTrue":
                    code = "ERROR_CODE_0016";
                    description = "true값만 들어올 수 있습니다. 다시 시도해 주세요.";
                    break;
                case "DecimalMax":
                    code = "ERROR_CODE_0017";
                    description = "기준치보다 큰거나 같은 입니다. 다시 시도해 주세요.";
                    break;
                case "DecimalMin":
                    code = "ERROR_CODE_0018";
                    description = "기준치보다 작거나 같은 숫자 입니다. 다시 시도해 주세요.";
                    break;
                default:
                    break;
            }
        }
        return new ResponseEntity<>(new ErrorResponseDTO(code, description, detail), HttpStatus.BAD_REQUEST);
    }
}
