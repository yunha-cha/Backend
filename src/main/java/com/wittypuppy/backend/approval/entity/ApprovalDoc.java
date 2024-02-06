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
@Entity(name = "APPROVAL_DOC")
@Table(name = "tbl_approval_document")
public class ApprovalDoc {
    @Id
    @Column(name = "approval_document_code")
    private Long approvalDocCode;

    @Column(name = "approval_form")
    private String approvalForm;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "approval_request_date")
    private LocalDateTime approvalRequestDate;

    @Column(name = "whether_saving_approval")
    private String whetherSavingApproval;
}
