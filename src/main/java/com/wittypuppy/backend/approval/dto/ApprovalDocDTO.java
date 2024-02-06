package com.wittypuppy.backend.approval.dto;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ApprovalDocDTO {

    private Long approvalDocCode;
    private String approvalForm;
    private EmployeeDTO employeeDTO;
    private LocalDateTime approvalRequestDate;
    private String whetherSavingApproval;
}
