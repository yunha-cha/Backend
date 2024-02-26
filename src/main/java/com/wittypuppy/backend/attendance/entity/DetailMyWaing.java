package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name ="ATTENDANCE_DETAIL_WAITING")
@Table(name = "tbl_approval_line")
public class DetailMyWaing {
    @Id
    @Column(name = "approval_line_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalLineCode;

    @JoinColumn(name = "approval_document_code")
    @OneToOne
    private ApprovalDocument approvalLineDocumentCode;

    @JoinColumn(name = "employee_code")
    @OneToOne
    private Employee lineEmployeeCode;

    @Column(name = "approval_process_order", columnDefinition = "BIGINT")
    private Long approvalProcessOrder;

    @Column(name = "approval_process_status", columnDefinition = "VARCHAR(100)")
    private String approvalProcessStatus;

    @Column(name = "approval_process_date", columnDefinition = "DATETIME")
    private LocalDateTime approvalProcessDate;

    @Column(name = "approval_rejected_reason", columnDefinition = "VARCHAR(500)")
    private String approvalRejectedReason;



    @Column(name = "on_leave_title", columnDefinition = "VARCHAR(100)")
    private String onLeaveTitle;

    @Column(name = "kind_of_on_leave", columnDefinition = "VARCHAR(100)")
    private String kindOfOnLeave;

    @Column(name = "on_leave_start_date", columnDefinition = "DATETIME")
    private LocalDateTime onLeaveStartDate;

    @Column(name = "on_leave_end_date", columnDefinition = "DATETIME")
    private LocalDateTime onLeaveEndDate;

    @Column(name = "on_leave_reason", columnDefinition = "VARCHAR(3000)")
    private String onLeaveReason;

    @Column(name = "overwork_title")
    private String overworkTitle;

    @Column(name = "kind_of_overwork")
    private String kindOfOverwork;

    @Column(name = "overwork_date")
    private LocalDateTime overworkDate;

    @Column(name = "overwork_start_time")
    private LocalDateTime overworkStartTime;

    @Column(name = "overwork_end_time")
    private LocalDateTime overworkEndTime;

    @Column(name = "overwork_reason")
    private String overworkReason;

    @Column(name = "software_title")
    private String softwareTitle;

    @Column(name = "kind_of_software")
    private String kindOfSoftware;

    @Column(name = "software_reason")
    private String softwareReason;

    @Column(name = "software_start_date")
    private LocalDateTime softwareStartDate;

    @Column(name = "work_type_form")
    private String workTypeForm;

    @Column(name = "work_type_title")
    private String workTypeTitle;

    @Column(name = "work_type_start_date")
    private LocalDateTime workTypeStartDate;

    @Column(name = "work_type_end_date")
    private LocalDateTime workTypeEndDate;

    @Column(name = "work_type_place")
    private String workTypePlace;

    @Column(name = "work_type_reason")
    private String workTypeReason;
}
