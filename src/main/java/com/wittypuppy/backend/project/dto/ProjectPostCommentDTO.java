package com.wittypuppy.backend.project.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ProjectPostCommentDTO {
    private Long projectPostCommentCode;

    private Long projectPostCode;

    private Long projectPostCommentContent;

    private Long projectPostCommentCreationDate;

    private Long projectPostMemberCode;

    private List<ProjectPostCommentFileDTO> projectPostCommentFileDTOList;
}
