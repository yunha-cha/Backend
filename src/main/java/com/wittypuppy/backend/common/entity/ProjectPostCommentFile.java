package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_project_post_comment_file")
public class ProjectPostCommentFile {
    @Id
    @Column(name = "project_post_comment_file_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostCommentFileCode;

    @Column(name = "project_post_comment_code", columnDefinition = "BIGINT")
    private Long projectPostCommentCode;

    @Column(name = "project_post_comment_file_og_file", columnDefinition = "VARCHAR(500)")
    private String projectPostCommentFileOgFile;

    @Column(name = "project_post_comment_file_changed_file", columnDefinition = "VARCHAR(500)")
    private String projectPostCommentFileChangedFile;

    @Column(name = "project_post_comment_file_creation_date", columnDefinition = "DATETIME")
    private LocalDateTime projectPostCommentFileCreationDate;
}
