package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_REPRESENT")
@Table(name = "tbl_approval_represent")
public class ApprovalRepresent {
    @Id
    @Column(name = "approval_represent_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalRepresentCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @JoinColumn(name = "assignee_code")
    @ManyToOne
    private LoginEmployee assignee;

    @JoinColumn(name = "representative_code")
    @ManyToOne
    private LoginEmployee representative;
}
