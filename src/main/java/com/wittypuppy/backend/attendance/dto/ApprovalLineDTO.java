package com.wittypuppy.backend.attendance.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ApprovalLineDTO {

    private Long approvalLineCode;

    private Long approvalDocumentCode;

    private Long employeeCode;

    private Long approvalProcessOrder;

    private String approvalProcessStatus;

    private LocalDateTime approvalProcessDate;

    private String approvalRejectedReason;
}
