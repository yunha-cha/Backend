package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.Employee.dto.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ApprovalDocumentDTO {

    private Long approvalDocumentCode;
    private String approvalForm;
    private EmployeeDTO documentEmployeeCode;
    private LocalDateTime approvalRequestDate;

}