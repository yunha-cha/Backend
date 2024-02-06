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
@Entity(name = "APPROVAL_COMMENT")
@Table(name = "tbl_approval_comment")
public class ApprovalComment {
    @Id
    @Column(name = "approval_comment_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalCommentCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private LoginEmployee loginEmployee;

    @Column(name = "approval_comment_content")
    private String approvalCommentContent;

    @Column(name = "approval_comment_date")
    private LocalDateTime approvalCommentDate;
}
