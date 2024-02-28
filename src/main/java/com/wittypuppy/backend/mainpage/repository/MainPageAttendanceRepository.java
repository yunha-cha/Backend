package com.wittypuppy.backend.mainpage.repository;

import com.wittypuppy.backend.attendance.entity.InsertAttendanceManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainPageAttendanceRepository extends JpaRepository<InsertAttendanceManagement, Long> {


    InsertAttendanceManagement findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(int employeeNum);
}
