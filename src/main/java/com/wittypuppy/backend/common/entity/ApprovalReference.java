package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_approval_reference")
public class ApprovalReference {
    @Id
    @Column(name = "approval_reference_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalReferenceCode;

    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "whether_checked_approval", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String whetherCheckedApproval;
}
