package com.wittypuppy.backend.attendance.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ApprovalDocumentDTO {

    private Long approvalDocumentCode;
    private String approvalForm;
    private Long employeeCode;
    private LocalDateTime apprvoalRequestDate;

}