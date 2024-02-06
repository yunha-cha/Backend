package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.approval.dto.EmployeeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_ON_LEAVE")
@Table(name = "tbl_on_leave")
public class OnLeave {
    @Id
    @Column(name = "on_leave_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long onLeaveCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @Column(name = "on_leave_title")
    private String onLeaveTitle;

    @JoinColumn(name = "remaining_on_leave")
    @ManyToOne
    private Employee onLeaveCount;

    @Column(name = "kind_of_on_leave")
    private String kindOfOnLeave;

    @Column(name = "on_leave_start_date")
    private LocalDateTime onLeaveStartDate;

    @Column(name = "on_leave_end_date")
    private LocalDateTime onLeaveEndDate;

    @Column(name = "on_leave_reason")
    private String onLeaveReason;
}
