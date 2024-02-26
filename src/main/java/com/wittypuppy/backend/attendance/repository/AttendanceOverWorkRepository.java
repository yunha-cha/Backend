package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.Overwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceOverWorkRepository extends JpaRepository<Overwork, Long> {

}
