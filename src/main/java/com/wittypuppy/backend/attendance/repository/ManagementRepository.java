package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ManagementRepository extends JpaRepository <AttendanceManagement, Long> {


    @Query(value = "SELECT " +
            "A.attendance_management_code, " +
            "A.attendance_management_arrival_time, " +
            "A.attendance_management_departure_time, " +
            "A.attendance_management_state, " +
            "A.attendance_management_work_day, " +
            "A.employee_code, " +
            "B.employee_name," +
            "C.attendance_work_type_status " +
            "FROM tbl_attendance_management A " +
            "LEFT JOIN tbl_employee B ON A.employee_code = B.employee_code " +
            "LEFT JOIN tbl_attendance_work_type C ON A.attendance_management_code = C.attendance_management_code " +
            "WHERE A.employee_code = :employeeCode " +
            "AND A.attendance_management_work_day = CURDATE() " +
            "ORDER BY A.attendance_management_code ASC " +
            "LIMIT 1",
            nativeQuery = true)
    AttendanceManagement attendanceCommute(int employeeCode);




    //사용한 연차 수량
    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :employeeCode " +
            "AND vacation_used_status = 'Y' " +
            "AND vacation_type = '연차' " +
            "AND vacation_expiration_date > NOW()",
            nativeQuery = true)
    Long attendanceUseVacation(int employeeCode);


    //사용한 반차 수량
    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :employeeCode " +
            "AND vacation_used_status = 'Y' " +
            "AND vacation_type = '반차' " +
            "AND vacation_expiration_date > NOW()",
            nativeQuery = true)
    Long attendanceUseHalfVacation(int employeeCode);



    @Query(value = "SELECT " +
            "A.attendance_management_arrival_time, " +
            "A.attendance_management_departure_time, " +
            "A.attendance_management_code, " +
            "A.attendance_management_state, " +
            "A.attendance_management_work_day," +
            "A.employee_code, " +
            "B.attendance_work_type_status " +
            "FROM tbl_attendance_management A " +
            "LEFT JOIN tbl_attendance_work_type B " +
            "ON A.attendance_management_code = B.attendance_management_code " +
            "WHERE A.employee_code = :employeeCode " +
            "AND DATE_FORMAT(A.attendance_management_work_day, '%Y-%m') = :yearMonth",
            nativeQuery = true)
    Page<AttendanceManagement> attendanceList(String yearMonth, int employeeCode, Pageable paging);



    @Query(value = "SELECT " +
            "A.attendance_management_arrival_time, " +
            "A.attendance_management_departure_time, " +
            "A.attendance_management_code, " +
            "A.attendance_management_state, " +
            "A.attendance_management_work_day," +
            "A.employee_code, " +
            "B.attendance_work_type_status " +
            "FROM tbl_attendance_management A " +
            "LEFT JOIN tbl_attendance_work_type B ON A.attendance_management_code = B.attendance_management_code " +
            "WHERE A.employee_code = :employeeCode " +
            "AND DATE_FORMAT(A.attendance_management_work_day, '%Y-%m') = :yearMonth",
            nativeQuery = true)
    List<AttendanceManagement> normal(int employeeCode, String yearMonth);
}
