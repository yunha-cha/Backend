package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Project_DepartmentRepository")
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
