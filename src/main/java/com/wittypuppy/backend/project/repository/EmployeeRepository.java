package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Project_EmployeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDepartment_DepartmentCode(Long departmentCode);
    List<Employee> findAllByEmployeeRetirementDateIsNull();
}
