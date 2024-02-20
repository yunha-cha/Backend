package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.sql.ast.tree.expression.Over;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_DOC")
@Table(name = "tbl_approval_document")
public class ApprovalDoc {
    @Id
    @Column(name = "approval_document_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalDocCode;

    @Column(name = "approval_form")
    private String approvalForm;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private LoginEmployee employeeCode;

    @Column(name = "approval_request_date")
    private LocalDateTime approvalRequestDate;

    @Column(name = "whether_saving_approval")
    private String whetherSavingApproval;

    @Column(name = "approval_title")
    private String approvalTitle;
}
