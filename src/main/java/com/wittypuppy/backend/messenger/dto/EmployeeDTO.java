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

    private DepartmentDTO departmentDTO;

    private JobDTO jobDTO;

    private String employeeName;

    private LocalDateTime employeeRetirementDate;

    private List<ProfileDTO> profileDTOList;
}
