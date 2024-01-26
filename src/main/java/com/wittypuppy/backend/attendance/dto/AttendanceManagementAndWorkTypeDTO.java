package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


public class AttendanceManagementAndWorkTypeDTO {

    private Long attendanceManagementCode;

    private Long employeeCode;

    private LocalDateTime attendanceManagementArrivalTime;

    private LocalDateTime attendanceManagementDepartureTime;

    private String attendanceManagementState;

    private LocalDateTime attendanceManagementWorkDay;

    private AttendanceWorkType attendanceWorkTypeList;
}
