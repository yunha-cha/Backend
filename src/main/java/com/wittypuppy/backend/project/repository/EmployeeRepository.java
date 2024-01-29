package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDepartment_DepartmentCode(Long departmentCode);
}
