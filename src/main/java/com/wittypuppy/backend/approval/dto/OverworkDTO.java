package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime overworkDate;
    private LocalDateTime overworkStartTime;
    private LocalDateTime overworkEndTime;
    private String overworkReason;
}
