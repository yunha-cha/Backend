package com.wittypuppy.backend.project.service;


import com.wittypuppy.backend.project.entity.Employee;
import com.wittypuppy.backend.project.entity.ProjectPostMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectMemberDTO {
    private Long projectMemberCode;

    private Long projectCode;

    private Employee employee;

    private String projectMemberDeleteStatus;
}
