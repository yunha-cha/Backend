package com.wittypuppy.backend.attendance.entity;

import com.wittypuppy.backend.common.entity.AttendanceWorkType;
import com.wittypuppy.backend.common.entity.WorkType;
import com.wittypuppy.backend.common.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_approval_document")
public class ApprovalDocument {
    @Id
    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalDocumentCode;

    @Column(name = "approval_form", columnDefinition = "VARCHAR(100)")
    private String approvalForm;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "apprvoal_request_date", columnDefinition = "DATETIME")
    private LocalDateTime apprvoalRequestDate;

}