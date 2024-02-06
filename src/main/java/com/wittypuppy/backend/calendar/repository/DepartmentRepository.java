package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Calendar_DepartmentRepository")
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
