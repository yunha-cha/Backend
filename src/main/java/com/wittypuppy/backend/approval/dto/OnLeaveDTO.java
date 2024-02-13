package com.wittypuppy.backend.approval.dto;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OnLeaveDTO {
    private Long onLeaveCode;
    private Long approvalDocCode;
    private String onLeaveTitle;
    private EmployeeDTO onLeaveCount;
    private String kindOfOnLeave;
    private Date onLeaveStartDate;
    private Date onLeaveEndDate;
    private String onLeaveReason;
}
