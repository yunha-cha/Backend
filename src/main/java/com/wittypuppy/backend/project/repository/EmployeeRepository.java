package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Department;
import com.wittypuppy.backend.project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Project_EmployeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
        List<Employee> findAllByEmployeeRetirementDateIsNull();
        Optional<Employee> findByEmployeeCodeAndEmployeeRetirementDateIsNull(Long employeeCode);
}
