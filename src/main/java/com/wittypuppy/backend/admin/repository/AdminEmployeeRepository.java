package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminEmployeeRepository extends JpaRepository<Employee,Long> {

    List<Employee> findAllByEmployeeRetirementDateIsNullOrDepartment_DepartmentNameAndEmployeeRetirementDateIsNull(String status);
    List<Employee> findAllByDepartment_DepartmentName(String status);


    @Query("SELECT e FROM ADMIN_EMPLOYEE e " +
            "JOIN e.department d " +
            "JOIN ADMIN_DEPARTMENT pd ON d.parentDepartmentCode = pd.departmentCode " +
            "WHERE pd.departmentName = :departmentName")
    List<Employee> findEmployee(@Param("departmentName") String departmentName);
}
