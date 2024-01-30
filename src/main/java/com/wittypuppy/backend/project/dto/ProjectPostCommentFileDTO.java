package com.wittypuppy.backend.project.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ProjectPostCommentFileDTO {
    private Long projectPostCommentFileCode;

    private Long projectPostCode;

    private String projectPostCommentFileOgFile;

    private String projectPostCommentFileChangedFile;

    private LocalDateTime projectPostCommentFileCreationDate;
}
