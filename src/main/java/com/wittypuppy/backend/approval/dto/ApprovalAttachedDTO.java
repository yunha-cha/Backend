package com.wittypuppy.backend.approval.dto;

import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalAttachedDTO {
    private Long approvalAttachedCode;
    private ApprovalDocDTO approvalDocDTO;
    private String approvalOgFile;
    private String approvalChangedFile;
    private LocalDateTime approvalAttachedDate;
    private String whetherDeletedApprovalAttached;
}
