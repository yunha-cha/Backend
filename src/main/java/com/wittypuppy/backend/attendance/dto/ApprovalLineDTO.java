package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.Employee.dto.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ApprovalLineDTO {

    private Long approvalLineCode;

    private ApprovalDocumentDTO approvalLineDocumentCode;

    private EmployeeDTO lineEmployeeCode;

    private Long approvalProcessOrder;

    private String approvalProcessStatus;

    private LocalDateTime approvalProcessDate;

    private String approvalRejectedReason;

    private int countWaiting; // 대기 상태인 행의 갯수
}
