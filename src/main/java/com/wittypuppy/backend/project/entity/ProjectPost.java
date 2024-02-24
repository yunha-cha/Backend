package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "PROJECT_PROJECT_POST")
@Table(name = "tbl_project_post")
public class ProjectPost {
    @Id
    @Column(name = "project_post_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectPostCode;

    @Column(name = "project_code")
    private Long projectCode;

    @Column(name = "project_post_creation_date")
    private Date projectPostCreationDate;

    @Column(name = "project_post_content")
    private String projectPostContent;

    @Column(name = "project_post_type")
    private String projectPostType;

    @Column(name = "project_post_title")
    private String projectPostTitle;

    @JoinColumn(name = "project_member_code")
    @ManyToOne
    private ProjectMember projectMember;

    @JoinColumn(name = "project_post_file_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ProjectPostFile> projectPostFileList;
}
