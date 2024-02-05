package com.wittypuppy.backend.approval.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ApprovalReferenceDTO {
    private Long approvalReferenceCode;
    private ApprovalDocDTO approvalDocDTO;
    private EmployeeDTO employeeDTO;
    private String whetherCheckedApproval;
}
