package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "PROJECT_PROJECT_POST_FILE")
@Table(name = "tbl_project_post_file")
public class ProjectPostFile {
    @Id
    @Column(name = "project_post_file_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostFileCode;

    @Column(name = "project_post_file_og_file")
    private String projectPostFileOgFile;

    @Column(name = "project_post_file_changed_file")
    private String projectPostFileChangedFile;

    @Column(name = "project_post_file_creation_date")
    private Date projectPostFileCreationDate;

    @Column(name = "project_post_code")
    private Long projectPostCode;
}
