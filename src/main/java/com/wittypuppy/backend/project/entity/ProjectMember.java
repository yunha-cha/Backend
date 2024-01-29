package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "PROJECT_PROJECT_MEMBER")
@Table(name = "tbl_project_member")
public class ProjectMember {
    @Id
    @Column(name = "project_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberCode;

    @Column(name="project_code")
    private Long projectCode;

    @JoinColumn(name="employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "project_member_delete_status")
    private String projectMemberDeleteStatus;

    @JoinColumn(name="project_member_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProjectPostMember> projectPostMemberList;

}