package com.wittypuppy.backend.approval.dto;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
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
