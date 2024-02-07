package com.wittypuppy.backend.approval.dto;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class AdditionalApprovalLineDTO {
    private Long approvalLineCode;
    private Long approvalDocCode;
    private Long employeeCode;
    private Long approvalProcessOrder;
    private String ApprovalProcessStatus;
    private LocalDateTime approvalProcessDate;
    private String approvalRejectedReason;
}
