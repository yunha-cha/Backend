package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommuteWorkTypeRepository extends  JpaRepository<AttendanceWorkType, Integer>{

//    @Query(value = "SELECT " +
//            "A.attendance_work_type_status, " +
//            "A.approval_document_code, " +
//            "A.attendance_work_type_code, " +
//            "A.attendance_management_code, " +
//            "A.employee_code " +
//            "FROM tbl_attendance_work_type A " +
//            "Left JOIN tbl_attendance_management B ON A.employee_code = B.employee_code " +
//            "WHERE B.employee_code = :employeeCode " +
//            "AND DATE_FORMAT(B.attendance_management_work_day, '%Y-%m') = :yearMonth",
//            nativeQuery = true)
//    Page<AttendanceWorkType> attendanceList(String yearMonth, int employeeCode, Pageable paging);


//    List<AttendanceWorkType> workType(int employeeCode);
}
