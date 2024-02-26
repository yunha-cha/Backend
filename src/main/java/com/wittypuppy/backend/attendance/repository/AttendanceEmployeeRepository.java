package com.wittypuppy.backend.attendance.repository;


import com.wittypuppy.backend.attendance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceEmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findByEmployeeCode(int employeeCode);


}
