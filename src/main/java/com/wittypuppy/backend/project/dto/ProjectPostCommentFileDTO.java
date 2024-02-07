package com.wittypuppy.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fileCreationDate;
}
