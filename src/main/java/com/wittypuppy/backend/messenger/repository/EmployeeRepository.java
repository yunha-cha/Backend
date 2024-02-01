package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeCodeAndEmployeeRetirementDateIsNull(Long employeeCode);

    List<Employee> findAllByEmployeeRetirementDateIsNull();

    List<Employee> findAllByEmployeeRetirementDateIsNullAndEmployeeCodeIn(List<Long> employeeCodeList);
}
