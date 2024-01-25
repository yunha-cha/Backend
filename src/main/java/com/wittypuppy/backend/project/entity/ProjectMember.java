package com.wittypuppy.backend.project.entity;

import com.wittypuppy.backend.common.entity.ProjectPostMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "PROJECT_PROJECT_MEMBER")
@Table(name = "tbl_project_member")
public class ProjectMember {
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
}
