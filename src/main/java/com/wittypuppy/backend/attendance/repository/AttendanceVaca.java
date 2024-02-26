package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.Employee;
import com.wittypuppy.backend.attendance.entity.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceVaca extends JpaRepository<Vacation, Long> {


}
