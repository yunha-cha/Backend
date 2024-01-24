package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
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

    @JoinColumn(name = "project_member_code")
    @OneToMany
    private List<ProjectPostMember> projectPostMemberList;
}
