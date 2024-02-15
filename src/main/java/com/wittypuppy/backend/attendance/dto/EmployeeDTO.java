package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.Employee.dto.User;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class EmployeeDTO {

    private Long employeeCode;

    private DepartmentDTO departmentCode;

    private String employeeName;

    private LocalDateTime employeeJoinDate;

    private Long employeeAssignedCode;

}
