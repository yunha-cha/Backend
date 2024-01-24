package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_approval_comment")
public class ApprovalComment {
    @Id
    @Column(name = "approval_comment_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalCommentCode;

    @Column(name = "approval_document_code",columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @Column(name = "employee_code",columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "approval_comment_content",columnDefinition = "VARCHAR(500)")
    private String approvalCommentContent;

    @Column(name = "approval_comment_date",columnDefinition = "DATETIME")
    private LocalDateTime approvalCommentDate;
}
