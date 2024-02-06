package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<ProfileDTO> profileList;
}
