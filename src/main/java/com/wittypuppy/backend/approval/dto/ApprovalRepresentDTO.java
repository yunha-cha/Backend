package com.wittypuppy.backend.approval.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ApprovalRepresentDTO {
    private Long approvalRepresentCode;
    private ApprovalDocDTO approvalDocDTO;
    private EmployeeDTO representativeCode;
    private EmployeeDTO assigneeCode;
}
