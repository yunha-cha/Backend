package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name ="ATTENDANCE_LINE")
@Table(name = "tbl_approval_line")
public class ApprovalLine {
    @Id
    @Column(name = "approval_line_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalLineCode;

    @JoinColumn(name = "approval_document_code")
    @OneToOne
    private ApprovalDocument approvalLineDocumentCode;

    @JoinColumn(name = "employee_code")
    @OneToOne
    private Employee lineEmployeeCode;

    @Column(name = "approval_process_order", columnDefinition = "BIGINT")
    private Long approvalProcessOrder;

    @Column(name = "approval_process_status", columnDefinition = "VARCHAR(100)")
    private String approvalProcessStatus;

    @Column(name = "approval_process_date", columnDefinition = "DATETIME")
    private LocalDateTime approvalProcessDate;

    @Column(name = "approval_rejected_reason", columnDefinition = "VARCHAR(500)")
    private String approvalRejectedReason;
}
