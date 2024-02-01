package com.wittypuppy.backend.mail.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter@Getter@ToString
public class EmployeeDTO {
    private Long employeeCode;
    private String employeeId;
    public EmployeeDTO(Long employeeCode){
        this.employeeCode = employeeCode;
    }
}
