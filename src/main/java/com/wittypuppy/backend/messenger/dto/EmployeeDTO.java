package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.util.Date;
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

    private Date employeeRetirementDate;

    private List<ProfileDTO> profileList;
}
