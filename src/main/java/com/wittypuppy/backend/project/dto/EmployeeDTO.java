package com.wittypuppy.backend.project.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDTO {
    private Long employeeCode;

    private Long departmentCode;
}
