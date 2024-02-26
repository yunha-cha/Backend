package com.wittypuppy.backend.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name ="ATTENDANCE_ATTENDANCE_INSERT")
@Table(name = "tbl_attendance_management")
public class InsertAttendanceManagement {
    @Id
    @Column(name = "attendance_management_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceManagementCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee attendanceEmployeeCode;

    @Column(name = "attendance_management_arrival_time", columnDefinition = "DATETIME")
    private LocalDateTime attendanceManagementArrivalTime;

    @Column(name = "attendance_management_departure_time", columnDefinition = "DATETIME")
    private LocalDateTime attendanceManagementDepartureTime;

    @Column(name = "attendance_management_state", columnDefinition = "VARCHAR(100)")
    private String attendanceManagementState;

    @Column(name = "attendance_management_work_day", columnDefinition = "DATE")
    private LocalDate attendanceManagementWorkDay;


}
