package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.Employee;
import com.wittypuppy.backend.attendance.entity.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceVaca extends JpaRepository<Vacation, Long> {

    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :emp " +
            "AND vacation_expiration_date > NOW() " +
            "AND YEAR(vacation_creation_date) = YEAR(NOW()) ",
            nativeQuery = true)
    int findCount(int emp);

    @Query(value = "SELECT * " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :emp " +
            "AND vacation_expiration_date > NOW() " +
            "ORDER BY vacation_code DESC " +
            "LIMIT 1 ",
            nativeQuery = true)
    Vacation underCount(int emp);




}
