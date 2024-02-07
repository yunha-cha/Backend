package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime employeeRetirementDate;

    private String profileImageURL;
}
