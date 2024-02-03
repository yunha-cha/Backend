package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "ATTENDANCE_ON_LEAVE")
@Table(name = "tbl_on_leave")
public class OnLeave {
    @Id
    @Column(name = "on_leave_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long onLeaveCode;

    @JoinColumn(name = "approval_document_code")
    @OneToOne
    private ApprovalDocument leaveApprovalDocumentCode;

    @Column(name = "on_leave_title", columnDefinition = "VARCHAR(100)")
    private String onLeaveTitle;

    @Column(name = "remaining_on_leave", columnDefinition = "BIGINT")
    private Long remainingOnLeave;

    @Column(name = "kind_of_on_leave", columnDefinition = "VARCHAR(100)")
    private String kindOfOnLeave;

    @Column(name = "on_leave_start_date", columnDefinition = "DATETIME")
    private LocalDateTime onLeaveStartDate;

    @Column(name = "on_leave_end_date", columnDefinition = "DATETIME")
    private LocalDateTime onLeaveEndDate;

    @Column(name = "on_leave_reason", columnDefinition = "VARCHAR(3000)")
    private String onLeaveReason;
}
