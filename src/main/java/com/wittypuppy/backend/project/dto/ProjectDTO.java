package com.wittypuppy.backend.project.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProjectDTO {
    private Long projectCode;

    private Long projectManagerCode;

    private String projectTitle;

    private String projectDescription;

    private String projectProgressStatus;

    private LocalDateTime projectDeadline;

    private String projectLockedStatus;
}
