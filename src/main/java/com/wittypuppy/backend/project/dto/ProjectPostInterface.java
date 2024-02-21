package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.util.Date;

public interface ProjectPostInterface {
    Long getProjectCode();
    Long getProjectPostCode();
    String getProjectPostStatus();
    String getProjectPostPriority();
    String getProjectPostTitle();
    Date getProjectPostCreationDate();
    Date getProjectPostModifyDate();
    Date getProjectPostDueDate();
    String getEmployeeName();
    String getEmployeeDeptName();
    Long getProjectPostMemberCount();
}
