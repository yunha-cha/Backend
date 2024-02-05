package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ApprovalLineDTO {
    private Long approvalLineCode;
    private ApprovalDocDTO approvalDocDTO;
    private EmployeeDTO employeeDTO;
    private Long approvalProcessOrder;
    private String ApprovalProcessStatus;
    private LocalDateTime approvalProcessDate;
    private String approvalRejectedReason;
}
