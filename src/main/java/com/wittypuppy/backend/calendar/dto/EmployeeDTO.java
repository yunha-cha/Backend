package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EmployeeDTO {
    private Long employeeCode;

    private DepartmentDTO department;

    private JobDTO job;

    private String employeeName;

    private Date employeeRetirementDate;

    private Long employeeAssignedCode;

    private String profileImageURL;
}
