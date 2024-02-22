package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OverworkDTO {
    private Long overworkCode;
    private Long approvalDocCode;
    private String overworkTitle;
    private String kindOfOverwork;
    private Date overworkDate;
    private Time overworkStartTime;
    private Time overworkEndTime;
    private String overworkReason;
    private String approvalTitle;
}
