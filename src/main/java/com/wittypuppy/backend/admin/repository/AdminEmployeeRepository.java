package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEmployeeRepository extends JpaRepository<Employee,Long> {

}
