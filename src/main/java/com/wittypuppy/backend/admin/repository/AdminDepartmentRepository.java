package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminDepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentName(String departmentName);
}
