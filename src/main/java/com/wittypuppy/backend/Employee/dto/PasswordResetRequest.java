package com.wittypuppy.backend.Employee.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class PasswordResetRequest {
    private String employeeId; // 또는 사용자 아이디에 맞게 변경
    private String employeeEmail;
}
