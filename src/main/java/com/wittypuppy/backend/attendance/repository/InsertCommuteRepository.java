package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.InsertAttendanceManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsertCommuteRepository  extends JpaRepository<InsertAttendanceManagement, Long> {
    InsertAttendanceManagement findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(int employeeNum);


}
