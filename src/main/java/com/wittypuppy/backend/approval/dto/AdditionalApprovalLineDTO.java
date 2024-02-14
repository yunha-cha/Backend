package com.wittypuppy.backend.approval.dto;

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
    private String approvalProcessStatus;
    private LocalDateTime approvalProcessDate;
    private String approvalRejectedReason;
}
