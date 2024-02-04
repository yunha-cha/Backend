package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_attendance_management")
public class AttendanceManagement {
    @Id
    @Column(name = "attendance_management_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceManagementCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "attendance_management_arrival_time", columnDefinition = "DATETIME")
    private LocalDateTime attendanceManagementArrivalTime;

    @Column(name = "attendance_management_departure_time", columnDefinition = "DATETIME")
    private LocalDateTime attendanceManagementDepartureTime;

    @Column(name = "attendance_management_state", columnDefinition = "VARCHAR(100)")
    private String attendanceManagementState;

    @Column(name = "attendance_management_work_day", columnDefinition = "DATE")
    private LocalDateTime attendanceManagementWorkDay;

    @JoinColumn(name = "attendence_management_code")
    @OneToMany
    private List<AttendanceWorkType> attendanceWorkTypeList;
}
