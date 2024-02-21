package com.wittypuppy.backend.attendance.dto;

import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class EmployeeDTO  {

    private int employeeCode;

    private DepartmentDTO departmentCode;

    private String employeeName;

    private LocalDateTime employeeJoinDate;

    private Long employeeAssignedCode;

    private LocalDateTime vacationCreationDate;

    private LocalDateTime vacationExpirationDate;

    private String vacationCreationReason;


}
