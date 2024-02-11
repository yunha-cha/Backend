package com.wittypuppy.backend.common.entity;

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
public class ApprovalDoc {
    @Id
    @Column(name = "approval_document_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalDocumentCode;

    @Column(name="approval_form",columnDefinition = "VARCHAR(100)")
    private String approvalForm;

    @Column(name="employee_code",columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "approval_request_date",columnDefinition = "DATETIME")
    private LocalDateTime apprvoalRequestDate;

    @Column(name = "whether_saving_approval",columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String whetherSavingApproval;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<ApprovalComment> approvalCommentList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<ApprovalAttached> approvalAttachedList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<ApprovalLine> approvalLineList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<ApprovalReference> approvalReferenceList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<AttendanceWorkType> attendanceWorkTypeList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<ApprovalRepresent> approvalRepresentList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<OnLeave> onLeaveList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<Overwork> overworkList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<SoftwareUse> softwareUseList;

    @JoinColumn(name = "approval_document_code")
    @OneToMany
    private List<WorkType> workTypeList;
}
