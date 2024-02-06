package com.wittypuppy.backend.common.dto;

import com.wittypuppy.backend.attendance.dto.ApprovalLineDTO;
import com.wittypuppy.backend.attendance.dto.AttendanceManagementDTO;
import com.wittypuppy.backend.attendance.dto.VacationDTO;
import com.wittypuppy.backend.board.dto.PostDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ResponseDTO {
    private int status; // 상태코드값
    private String message; // 응답메시지
    private Object data; // 응답데이터

    public ResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseDTO(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public ResponseDTO(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        this.data = message;
    }
}
