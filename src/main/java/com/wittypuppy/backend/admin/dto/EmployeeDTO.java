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

    private String employeeName;
    private String employeeBirthDate;
    private String employeeResidentNumber;
    private String employeePhone;
    private String employeeAddress;
    private String employeeJoinDate;
    private String employeeRetirementDate;

    private String employeeId;
    private String employeePassword;

    private String employeeAssignedCode;
    private String employeeOnLeaveCount;
    private String employeeExternalEmail;
    private String employeeRole;
    public EmployeeDTO (Long employeeCode){
        this.employeeCode = employeeCode;
    }
}
