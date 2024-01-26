package com.wittypuppy.backend.attendance.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


public class ApprovalDocumentDTO {

    private Long approvalDocumentCode;
    private String approvalForm;
    private Long employeeCode;
    private LocalDateTime apprvoalRequestDate;

}