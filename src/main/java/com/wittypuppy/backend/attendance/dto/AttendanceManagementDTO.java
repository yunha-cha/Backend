package com.wittypuppy.backend.attendance.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


public class AttendanceManagementDTO {

    private Long attendanceManagementCode;

    private Long employeeCode;

    private LocalDateTime attendanceManagementArrivalTime;

    private LocalDateTime attendanceManagementDepartureTime;

    private String attendanceManagementState;

    private LocalDateTime attendanceManagementWorkDay;

}
