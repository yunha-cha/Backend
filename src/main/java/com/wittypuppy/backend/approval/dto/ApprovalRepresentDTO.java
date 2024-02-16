package com.wittypuppy.backend.approval.dto;

import com.wittypuppy.backend.Employee.dto.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ApprovalRepresentDTO {
    private Long approvalRepresentCode;
    private ApprovalDocDTO approvalDocDTO;
    private Date startDate;
    private Date endDate;
    private String representStatus;
    private User representativeCode;
    private User assigneeCode;
}
