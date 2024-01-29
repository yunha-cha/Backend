package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository("Calendar_EmployeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByEmployeeRetirementDateBefore(LocalDateTime currentDateTime);
}
