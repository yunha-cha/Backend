package com.wittypuppy.backend.attendance.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


public class EmployeeDTO {

    private Long employeeCode;

    private Long departmentCode;

    private String employeeName;

    private LocalDateTime employeeJoinDate;

    private Long employeeAssignedCode;

}
