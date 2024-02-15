package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CommuteWorkTypeRepository extends  JpaRepository<AttendanceWorkType, Integer>{

    @Query(value = "SELECT " +
            "A.attendance_management_code, " +
            "A.attendance_work_type_status, " +
            "A.attendance_work_type_code, " +
            "A.approval_document_code, " +
            "A.employee_code, " +
            "DATE_FORMAT(B.attendance_management_arrival_time, '%Y년%m월%d일 %H시%i분%s초') AS attendance_management_arrival_time, " +
            "B.attendance_management_departure_time, " +
            "B.attendance_management_state, " +
            "B.attendance_management_work_day " +
            "FROM tbl_attendance_work_type A " +
            "LEFT JOIN tbl_attendance_management B ON A.attendance_management_code = B.attendance_management_code " +
            "WHERE A.employee_code = :employeeCode " +
            "AND DATE_FORMAT(B.attendance_management_work_day, '%Y-%m') = :yearMonth" ,
            nativeQuery = true)
    Page<AttendanceWorkType> attendanceList(String yearMonth, Long employeeCode, Pageable paging);



}
