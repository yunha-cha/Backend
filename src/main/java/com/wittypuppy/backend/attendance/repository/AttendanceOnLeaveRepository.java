package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.OnLeave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceOnLeaveRepository extends JpaRepository<OnLeave, Long> {
}
