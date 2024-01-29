package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository("Calendar_EmployeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<List<Employee>> findAllByEmployeeRetirementDateBefore(LocalDateTime currentDateTime);
}
