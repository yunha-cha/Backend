package com.wittypuppy.backend.approval.dto;

import com.wittypuppy.backend.Employee.dto.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ApprovalRepresentDTO {
    private Long approvalRepresentCode;
    private ApprovalDocDTO approvalDocDTO;
    private User representativeCode;
    private User assigneeCode;
}
