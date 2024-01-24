package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_project_post_comment")
public class ProjectPostComment {
    @Id
    @Column(name = "project_post_comment_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostCommentCode;

    @Column(name = "project_post_code", columnDefinition = "BIGINT")
    private Long projectPostCode;

    @Column(name = "project_post_comment_content", columnDefinition = "VARCHAR(3000)")
    private String projectPostCommentContent;

    @Column(name = "project_post_comment_creation_date", columnDefinition = "DATETIME")
    private LocalDateTime projectPostCommentCreationDate;

    @JoinColumn(name = "project_post_comment_code")
    @OneToMany
    private List<ProjectPostCommentFile> projectPostCommentFileList;
}
