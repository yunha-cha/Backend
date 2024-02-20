package com.wittypuppy.backend.attendance.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class AttendanceResponseDTO {
    private int status; // 상태코드값
    private String message; // 응답메시지
    private Object data; // 응답데이터
    private Object data2; // 응답데이터
    private Object data3; // 응답데이터
    private Object data4; // 응답데이터


    public AttendanceResponseDTO(HttpStatus status, String message, Object data, Object data2, Object data3, Object data4) {
        this.status = status.value();
        this.message = message;
        this.data = data;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
    }


}
