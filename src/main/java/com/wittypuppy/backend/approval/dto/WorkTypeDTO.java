package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkTypeDTO {
    private Long workTypeCode;
    private Long approvalDocCode;
    private String workTypeForm;
    private String workTypeTitle;
    private LocalDateTime workTypeStartDate;
    private LocalDateTime workTypeEndDate;
    private String workTypePlace;
    private String workTypeReason;
}
