package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.AttendanceWorkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkTypeRepository extends JpaRepository <AttendanceWorkType, Integer>{

    List<AttendanceWorkType> typeAll();
}
