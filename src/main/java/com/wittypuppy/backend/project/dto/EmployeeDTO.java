package com.wittypuppy.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime employeeRetirementDate;

    private List<ProfileDTO> profileList;
}
