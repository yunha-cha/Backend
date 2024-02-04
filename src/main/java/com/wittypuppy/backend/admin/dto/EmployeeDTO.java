package com.wittypuppy.backend.admin.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
public class EmployeeDTO {
    private Long employeeCode;

    private DepartmentDTO employeeDepartment;
    private JobDTO employeeJob;
    private List<EducationDTO> educations;
    private List<CareerDTO> careers;

    private String employeeBirthDate;
    private String employeeJoinDate;
    private String employeeAddress;
    private String employeeId;
    private String employeeName;
    //private String employeePassword;
    private String employeePhone;
    private String employeeResidentNumber;
    private String employeeRetirementDate;
    private String employeeAssignedCode;
    private String employeeOnLeaveCount;
    private String employeeExternalEmail;
    private String employeeRole;
}
