package com.wittypuppy.backend.attendance.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class DetailMyWaitingDTO {

    private Long approvalLineCode;

    private ApprovalDocumentDTO approvalLineDocumentCode;

    private EmployeeDTO lineEmployeeCode;

    private Long approvalProcessOrder;

    private String approvalProcessStatus;

    private LocalDateTime approvalProcessDate;

    private String approvalRejectedReason;

    private String workTypeForm;

    private String workTypeTitle;

    private LocalDateTime workTypeStartDate;

    private LocalDateTime workTypeEndDate;

    private String workTypePlace;

    private String workTypeReason;

    private String overworkTitle;
    private String kindOfOverwork;
    private LocalDateTime overworkDate;
    private LocalDateTime overworkStartTime;
    private LocalDateTime overworkEndTime;
    private String overworkReason;

    private String onLeaveTitle;

    private String kindOfOnLeave;

    private LocalDateTime onLeaveStartDate;

    private LocalDateTime onLeaveEndDate;

    private String onLeaveReason;

    private String softwareTitle;
    private String kindOfSoftware;
    private String softwareReason;
    private LocalDateTime softwareStartDate;


}
