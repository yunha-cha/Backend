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
@Table(name = "tbl_approval_attached")
public class ApprovalAttached {
    @Id
    @Column(name = "approval_attached_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalAttachedCode;

    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    private Long approvalDocumentCode;

    @Column(name = "approval_og_file", columnDefinition = "VARCHAR(500)")
    private String approvalOgFile;

    @Column(name = "approval_changed_file", columnDefinition = "VARCHAR(500)")
    private String approvalChangedFile;

    @Column(name = "approval_attached_date", columnDefinition = "DATETIME")
    private LocalDateTime approvalAttachedDate;

    @Column(name = "whether_deleted_approval_attached", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String whetherDeletedApprovalAttached;
}
