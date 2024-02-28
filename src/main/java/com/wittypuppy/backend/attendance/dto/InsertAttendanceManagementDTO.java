package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.Employee.dto.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InsertAttendanceManagementDTO {

    private Long attendanceManagementCode;

    private EmployeeDTO attendanceEmployeeCode;

    private LocalDateTime attendanceManagementArrivalTime;

    private LocalDateTime attendanceManagementDepartureTime;

    private String attendanceManagementState;

    private LocalDate attendanceManagementWorkDay;



}
