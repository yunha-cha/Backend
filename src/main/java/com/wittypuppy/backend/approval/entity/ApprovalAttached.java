package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.calendar.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_ATTACHED")
@Table(name = "tbl_approval_attached")
public class ApprovalAttached {
    @Id
    @Column(name = "approval_attached_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalAttachedCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @Column(name = "approval_og_file")
    private String approvalOgFile;

    @Column(name = "approval_changed_file")
    private String approvalChangedFile;

    @Column(name = "approval_attached_date")
    private LocalDateTime approvalAttachedDate;

    @Column(name = "whether_deleted_approval_attached")
    private String whetherDeletedApprovalAttached;
}
