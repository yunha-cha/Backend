package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EmployeeDTO {
    private Long employeeCode;

    private DepartmentDTO departmentDTO;

    private JobDTO jobDTO;

    private String employeeName;

    private List<ProfileDTO> profileDTOList;
}
