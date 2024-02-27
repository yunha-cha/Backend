package com.wittypuppy.backend.mainpage.repository;

import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainPageAttendanceRepository extends JpaRepository<AttendanceManagement, Long> {


}
