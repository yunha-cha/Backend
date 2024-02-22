package com.wittypuppy.backend.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "PROJECT_PROJECT_MEMBER")
@Table(name = "tbl_project_member")
public class ProjectMember {
    @Id
    @Column(name = "project_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberCode;

    @Column(name = "project_code")
    private Long projectCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "project_member_delete_status")
    private String projectMemberDeleteStatus;

    public ProjectMember setProjectMemberCode(Long projectMemberCode) {
        this.projectMemberCode = projectMemberCode;
        return this;
    }

    public ProjectMember setProjectCode(Long projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public ProjectMember setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public ProjectMember setProjectMemberDeleteStatus(String projectMemberDeleteStatus) {
        this.projectMemberDeleteStatus = projectMemberDeleteStatus;
        return this;
    }

    public ProjectMember builder(){
        return new ProjectMember(projectMemberCode,projectCode,employee,projectMemberDeleteStatus);
    }
}
