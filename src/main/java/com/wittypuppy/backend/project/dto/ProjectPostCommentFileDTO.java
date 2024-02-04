package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostCommentFileDTO {
    private Long projectPostCommentCode;
    private Long projectPostCommentFileCode;
    private String fileURL;
    private LocalDateTime fileCreationDate;
}
