package com.wittypuppy.backend.attendance.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceManagementDTO {

    private Long attendanceManagementCode;

    private EmployeeDTO attendanceEmployeeCode;

    private LocalDateTime attendanceManagementArrivalTime;

    private LocalDateTime attendanceManagementDepartureTime;

    private String attendanceManagementState;

    private LocalDate attendanceManagementWorkDay;

}
