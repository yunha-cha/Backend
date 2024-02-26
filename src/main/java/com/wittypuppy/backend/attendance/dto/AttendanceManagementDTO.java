package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.Employee.dto.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceManagementDTO{

    private Long attendanceManagementCode;

    private EmployeeDTO attendanceEmployeeCode;

    private LocalDateTime attendanceManagementArrivalTime;

    private LocalDateTime attendanceManagementDepartureTime;

    private String attendanceManagementState;

    private LocalDate attendanceManagementWorkDay;

    private String attendanceWorkTypeStatus;

    private int normal; //정상 출퇴근 횟수

    private int late; //지각 횟수

    private int early; //조퇴 횟수



}
