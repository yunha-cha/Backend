package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.adminAttend.AdminEmployee;
import com.wittypuppy.backend.attendance.adminAttend.AdminEmployeeDTO;
import com.wittypuppy.backend.attendance.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceAdminEmployee extends JpaRepository<AdminEmployee, Integer> {

    @Query(value = "SELECT DISTINCT " +
            "B.employee_code, " +
            "B.employee_name, " +
            "B.employee_join_date, " +
            "B.department_code, " +
            "B.employee_assigned_code, " +
            "A.vacation_creation_reason, " +
            "A.vacation_creation_date, " +
            "C.department_name, " +
            "A.vacation_expiration_date " +
            "FROM tbl_employee B " +
            "LEFT JOIN tbl_vacation A ON A.employee_code = B.employee_code " +
            "LEFT JOIN tbl_department C ON B.department_code = C.department_code " +
            "WHERE YEAR(A.vacation_expiration_date) = YEAR(CURRENT_DATE()) + 1", nativeQuery = true)
    Page<AdminEmployee> findEmp(Pageable paging);



    @Query(value = "SELECT DISTINCT " +
            "B.employee_code, " +
            "B.employee_name, " +
            "B.employee_join_date, " +
            "B.department_code, " +
            "A.vacation_creation_reason, " +
            "B.employee_assigned_code, " +
            "A.vacation_creation_date, " +
            "C.department_name, " +
            "A.vacation_expiration_date " +
            "FROM tbl_employee B " +
            "LEFT JOIN tbl_vacation A ON A.employee_code = B.employee_code " +
            "LEFT JOIN tbl_department C ON B.department_code = C.department_code " +
            "WHERE A.vacation_expiration_date IS NULL OR YEAR(A.vacation_expiration_date) = YEAR(NOW())", nativeQuery = true)
    Page<AdminEmployee> findNo(Pageable paging);
}
