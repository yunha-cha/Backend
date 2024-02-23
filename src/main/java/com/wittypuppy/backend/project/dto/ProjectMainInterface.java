package com.wittypuppy.backend.project.dto;

import java.util.Date;

public interface ProjectMainInterface {
    Long getProjectCode();

    String getProjectManagerName();

    String getProjectManagerDeptName();

    String getProjectTitle();

    String getProjectDescription();

    String getProjectProgressStatus();

    Date getProjectDeadline();

    String getProjectLockedStatus();

    Long getMyParticipationStatus();
}
