package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.SoftwareUse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceSoft extends JpaRepository<SoftwareUse, Long> {
}
