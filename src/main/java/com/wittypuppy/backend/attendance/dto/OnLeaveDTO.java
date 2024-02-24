package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.attendance.entity.ApprovalDocument;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class OnLeaveDTO {

    private Long onLeaveCode;

    private ApprovalDocument leaveApprovalDocumentCode;

    private Long remainingOnLeave;

    private String onLeaveTitle;

    private String kindOfOnLeave;

    private LocalDateTime onLeaveStartDate;

    private LocalDateTime onLeaveEndDate;

    private String onLeaveReason;
}
