package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ApprovalDocDTO {

    private Long ApprovalDocCode;
    private String approvalForm;
    private EmployeeDTO employeeDTO;
    private LocalDateTime approvalRequestDate;
    private String whetherSavingApproval;
}
