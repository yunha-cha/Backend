package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.adminAttend.AdminEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceAdminEmployee extends JpaRepository<AdminEmployee, Integer> {

    @Query(value = "SELECT DISTINCT " +
            "B.employee_name, " +
            "B.department_code, " +
            "B.employee_join_date, " +
            "B.employee_code, " +
            "B.employee_role, " +
            "A.vacation_creation_reason, " +
            "A.vacation_creation_date, " +
            "C.department_name, " +
            "A.vacation_expiration_date " +
            "FROM " +
            "tbl_employee B " +
            "LEFT JOIN tbl_vacation A ON A.employee_code = B.employee_code " +
            "LEFT JOIN tbl_department C ON B.department_code = C.department_code " +
            "WHERE " +
            "(A.vacation_creation_date IS NOT NULL " +
            "AND YEAR(A.vacation_creation_date) = YEAR(CURRENT_DATE()) " +
            "AND DATEDIFF(CURRENT_DATE(), B.employee_join_date) > 365 " +
            "AND A.vacation_creation_date = ( " +
            "SELECT MAX(vacation_creation_date) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = B.employee_code " +
            ")) " +
            "OR " +
            "(A.vacation_creation_date IS NOT NULL " +
            "AND DATEDIFF(CURRENT_DATE(), B.employee_join_date) < 365 " +
            "AND A.vacation_creation_date = ( " +
            "SELECT MAX(vacation_creation_date) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = B.employee_code " +
            ") " +
            "AND DATE_ADD(A.vacation_creation_date, INTERVAL 1 MONTH) > CURRENT_DATE())", nativeQuery = true)
    Page<AdminEmployee> findEmp(Pageable paging);



    @Query(value = "SELECT " +
            "B.employee_name, " +
            "B.department_code, " +
            "B.employee_join_date, " +
            "B.employee_code, " +
            "B.employee_role, " +
            "A.vacation_creation_reason, " +
            "A.vacation_creation_date, " +
            "C.department_name, " +
            "A.vacation_expiration_date, " +
            "CASE " +
            "        WHEN A.vacation_expiration_date IS NULL AND DATE_ADD(B.employee_join_date, INTERVAL 1 MONTH) <= CURRENT_DATE() THEN NULL " +
            "        ELSE DATE_ADD(A.vacation_creation_date, INTERVAL 1 MONTH) " +
            "    END AS next_month_date " +
            "FROM " +
            "    tbl_employee B " +
            "        LEFT JOIN " +
            "    tbl_vacation A ON A.employee_code = B.employee_code " +
            "        LEFT JOIN " +
            "    tbl_department C ON B.department_code = C.department_code " +
            "WHERE " +
            "    (A.vacation_expiration_date IS NULL AND DATE_ADD(B.employee_join_date, INTERVAL 1 MONTH) <= CURRENT_DATE()) " +
            "    OR " +
            "    (DATEDIFF(CURRENT_DATE(), B.employee_join_date) < 365 " +
            "        AND A.vacation_creation_date = (SELECT MAX(vacation_creation_date) FROM tbl_vacation WHERE employee_code = B.employee_code) " +
            "        AND DATE_ADD(A.vacation_creation_date, INTERVAL 1 MONTH) < CURRENT_DATE()) " +
            "    OR " +
            "    (DATEDIFF(CURRENT_DATE(), B.employee_join_date) > 365 " +
            "        AND A.vacation_creation_date = (SELECT MAX(vacation_creation_date) FROM tbl_vacation WHERE employee_code = B.employee_code) " +
            "        AND YEAR(A.vacation_creation_date) != YEAR(CURRENT_DATE()));", nativeQuery = true)
    Page<AdminEmployee> findNo(Pageable paging);



    @Query(value = "SELECT\n" +
            "B.employee_name, " +
            "B.department_code, " +
            "B.employee_join_date, " +
            "B.employee_code, " +
            "B.employee_role, " +
            "A.vacation_creation_reason, " +
            "A.vacation_creation_date, " +
            "C.department_name, " +
            "A.vacation_expiration_date " +
            "FROM tbl_employee B" +
            "         LEFT JOIN tbl_vacation A ON A.employee_code = B.employee_code\n" +
            "         LEFT JOIN tbl_department C ON B.department_code = C.department_code\n" +
            "WHERE DATEDIFF(CURRENT_DATE(), B.employee_join_date) > 365\n" +
            "  AND A.vacation_creation_date = (\n" +
            "    SELECT MAX(vacation_creation_date)\n" +
            "    FROM tbl_vacation\n" +
            "    WHERE employee_code = B.employee_code\n" +
            ")\n" +
            "  AND YEAR(A.vacation_creation_date) != YEAR(CURRENT_DATE());", nativeQuery = true)
    List<AdminEmployee> noYearVacation();


    @Query(value = "SELECT " +
            "B.employee_name, " +
            "B.department_code, " +
            "B.employee_join_date, " +
            "B.employee_code, " +
            "B.employee_role, " +
            "A.vacation_creation_reason, " +
            "A.vacation_creation_date ," +
            "C.department_name, " +
            "A.vacation_expiration_date, " +
            "DATE_ADD(A.vacation_creation_date, INTERVAL 1 MONTH) AS next_month_date " +
            "FROM " +
            "    tbl_employee B " +
            "    LEFT JOIN tbl_vacation A ON A.employee_code = B.employee_code " +
            "    LEFT JOIN tbl_department C ON B.department_code = C.department_code " +
            "WHERE " +
            "    DATEDIFF(CURRENT_DATE(), B.employee_join_date) < 365 " +
            "    AND A.vacation_creation_date = ( " +
            "        SELECT MAX(vacation_creation_date) " +
            "        FROM tbl_vacation " +
            "        WHERE employee_code = B.employee_code " +
            "    ) " +
            "    AND DATE_ADD(A.vacation_creation_date, INTERVAL 1 MONTH) < CURRENT_DATE()", nativeQuery = true)
    List<AdminEmployee> noUnderVacation();


    @Query(value = "SELECT " +
            "B.employee_name, " +
            "B.department_code, " +
            "B.employee_join_date, " +
            "B.employee_code, " +
            "B.employee_role, " +
            "A.vacation_creation_reason, " +
            "A.vacation_creation_date ," +
            "C.department_name, " +
            "A.vacation_expiration_date " +
            "FROM " +
            "    tbl_employee B " +
            "    LEFT JOIN tbl_vacation A ON A.employee_code = B.employee_code " +
            "    LEFT JOIN tbl_department C ON B.department_code = C.department_code " +
            "WHERE " +
            "    A.vacation_expiration_date IS NULL " +
            "    AND DATE_ADD(B.employee_join_date, INTERVAL 1 MONTH) <= CURRENT_DATE()", nativeQuery = true)
    List<AdminEmployee> firstVacation();


    @Query(value = "SELECT " +
            "B.employee_name, " +
            "B.department_code, " +
            "B.employee_join_date, " +
            "B.employee_code, " +
            "B.employee_role, " +
            "A.vacation_creation_reason, " +
            "A.vacation_creation_date, " +
            "C.department_name, " +
            "A.vacation_expiration_date, " +
            "CASE " +
            "        WHEN A.vacation_expiration_date IS NULL AND DATE_ADD(B.employee_join_date, INTERVAL 1 MONTH) <= CURRENT_DATE() THEN NULL " +
            "        ELSE DATE_ADD(A.vacation_creation_date, INTERVAL 1 MONTH) " +
            "    END AS next_month_date " +
            "FROM " +
            "    tbl_employee B " +
            "        LEFT JOIN " +
            "    tbl_vacation A ON A.employee_code = B.employee_code " +
            "        LEFT JOIN " +
            "    tbl_department C ON B.department_code = C.department_code " +
            "WHERE " +
            "    (A.vacation_expiration_date IS NULL AND DATE_ADD(B.employee_join_date, INTERVAL 1 MONTH) <= CURRENT_DATE()) " +
            "    OR " +
            "    (DATEDIFF(CURRENT_DATE(), B.employee_join_date) < 365 " +
            "        AND A.vacation_creation_date = (SELECT MAX(vacation_creation_date) FROM tbl_vacation WHERE employee_code = B.employee_code) " +
            "        AND DATE_ADD(A.vacation_creation_date, INTERVAL 1 MONTH) < CURRENT_DATE()) " +
            "    OR " +
            "    (DATEDIFF(CURRENT_DATE(), B.employee_join_date) > 365 " +
            "        AND A.vacation_creation_date = (SELECT MAX(vacation_creation_date) FROM tbl_vacation WHERE employee_code = B.employee_code) " +
            "        AND YEAR(A.vacation_creation_date) != YEAR(CURRENT_DATE()));", nativeQuery = true)
    List<AdminEmployee> all();
}
