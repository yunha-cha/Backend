package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Messenger_EmployeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByEmployeeRetirementDateIsNull();
}
