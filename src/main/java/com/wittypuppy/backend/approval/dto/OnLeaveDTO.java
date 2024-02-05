package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OnLeaveDTO {
    private Long onLeaveCode;
    private ApprovalDocDTO approvalDocDTO;
    private String onLeaveTitle;
    private EmployeeDTO onLeaveCount;
    private String kindOfOnLeave;
    private LocalDateTime onLeaveStartDate;
    private LocalDateTime onLeaveEndDate;
    private String onLeaveReason;
}
