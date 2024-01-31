package com.wittypuppy.backend.Employee.repository;


import com.wittypuppy.backend.Employee.entity.LoginEmployeeRole;
import com.wittypuppy.backend.Employee.entity.EmployeeRolePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleRepository extends JpaRepository<LoginEmployeeRole, Long> {
}


