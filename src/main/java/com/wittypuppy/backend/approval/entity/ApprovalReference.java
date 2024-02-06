package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.calendar.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_REFERENCE")
@Table(name = "tbl_approval_reference")
public class ApprovalReference {
    @Id
    @Column(name = "approval_reference_code")
    private Long approvalReferenceCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "whether_checked_approval")
    private String whetherCheckedApproval;
}
