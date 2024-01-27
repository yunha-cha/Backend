package com.wittypuppy.backend.project.dto.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectMemberDTO {
    private Long projectMemberCode;

    private Long projectCode;

    private EmployeeDTO projectMember;

    private String projectMemberDeleteStatus;
}
