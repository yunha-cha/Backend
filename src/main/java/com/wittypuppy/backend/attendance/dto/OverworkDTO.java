package com.wittypuppy.backend.attendance.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OverworkDTO {
    private Long overworkCode;
    private Long overworkDocumentCode;
    private String overworkTitle;
    private String kindOfOverwork;
    private LocalDateTime overworkDate;
    private LocalDateTime overworkStartTime;
    private LocalDateTime overworkEndTime;
    private String overworkReason;
}
