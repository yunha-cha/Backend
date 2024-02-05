package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import com.wittypuppy.backend.attendance.entity.Vacation;
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
            "A.employee_code, B.employee_name " +
            "FROM tbl_attendance_management A " +
            "LEFT JOIN tbl_employee B ON A.employee_code = B.employee_code " +
            "WHERE A.employee_code = :employeeCode " +
            "AND A.attendance_management_work_day = CURDATE()",
            nativeQuery = true)
    AttendanceManagement attendanceCommute(Long employeeCode);


    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :employeeCode " +
            "AND vacation_used_status = 'N' " +
            "AND vacation_expiration_date > NOW()",
            nativeQuery = true)
    Long attendanceVacation(Long employeeCode);

    AttendanceManagement findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(Long employeeCode);


//    @Query(value = "SELECT " +
//            "A.approval_document_code, " +
//            "A.approval_line_code, " +
//            "A.approval_process_date, " +
//            "A.approval_process_order, " +
//            "A.employee_code, " +
//            "A.approval_process_status, " +
//            "A.approval_rejected_reason " +
//            "FROM tbl_approval_line A " +
//            "LEFT JOIN tbl_approval_document B ON A.employee_code = B.employee_code " +
//            "WHERE A.approval_process_status = '대기' " +
//            "AND A.employee_code = :employeeCode " +
//            "AND A.approval_process_order IN (SELECT approval_process_order - 1 FROM tbl_approval_line) " +
//            "AND ((A.approval_process_status IN ('결재'or '기안') " +
//            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
//            "                                           WHERE approval_process_status IN ('결재'or '기안'))) " +
//            "    OR " +
//            "    (A.approval_process_status IN ('대기'or '반려'or '회수') " +
//            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
//            "                                           WHERE approval_process_status IN ('대기'or '반려'or '회수'))))",
//            nativeQuery = true)
//    ApprovalLine attendanceWaiting(Long employeeCode);



}
