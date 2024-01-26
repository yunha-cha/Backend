package com.wittypuppy.backend.attendance.dto;

import jakarta.persistence.*;
import lombok.*;


public class AttendanceWorkTypeDTO {

    private Long attendanceWorkTypeCode;

    private Long employeeCode;

    private String attendanceWorkTypeStatus;

    private Long approvalDocumentCode;

    private Long attendanceManagementCode;
}
