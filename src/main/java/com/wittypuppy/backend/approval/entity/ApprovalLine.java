package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_LINE")
@Table(name = "tbl_approval_line")
public class ApprovalLine {
    @Id
    @Column(name = "approval_line_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalLineCode;

    @Column(name = "approval_document_code")
    private Long approvalDocCode;

    @Column(name = "employee_code")
    private Long employeeCode;

    @Column(name = "approval_process_order")
    private Long approvalProcessOrder;

    @Column(name = "approval_process_status")
    private String ApprovalProcessStatus;

    @Column(name = "approval_process_date")
    private LocalDateTime approvalProcessDate;

    @Column(name = "approval_rejected_reason")
    private String approvalRejectedReason;
}
