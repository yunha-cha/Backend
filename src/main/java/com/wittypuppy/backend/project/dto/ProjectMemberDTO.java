package com.wittypuppy.backend.project.dto;

import jakarta.persistence.*;

public class ProjectMemberDTO {
    private Long projectMemberCode;

    private Long projectCode;

    private Long employeeCode;

    private String projectMemberDeleteStatus;
}
