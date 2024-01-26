package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "PROJECT_PROJECT_MEMBER_AND_POST_MEMBER")
@Table(name = "tbl_project_member")
public class ProjectMemberAndPostMember {
    @Id
    @Column(name = "project_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberCode;

    @Column(name = "project_code", columnDefinition = "BIGINT")
    private Long projectCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "project_member_delete_status", columnDefinition = "VARCHAR(1)")
    private String projectMemberDeleteStatus;

    @JoinColumn(name = "project_member_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ProjectPostMember> projectPostMemberList;
}
