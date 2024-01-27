package com.wittypuppy.backend.project.dto.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostMemberDTO {
    private Long projectPostMemberCode;

    private Long projectPostCode;

    private EmployeeDTO projectMember;

    private String projectPostMemberDeleteStatus;
}
