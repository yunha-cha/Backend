package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name ="ATTENDANCE_DOCUMENT")
@Table(name = "tbl_approval_document")
public class ApprovalDocument {
    @Id
    @Column(name = "approval_document_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalDocumentCode;

    @Column(name = "approval_form", columnDefinition = "VARCHAR(100)")
    private String approvalForm;

    @JoinColumn(name = "employee_code")
    @OneToOne
    private Employee documentEmployeeCode;

    @Column(name = "approval_request_date", columnDefinition = "DATETIME")
    private LocalDateTime approvalRequestDate;

}