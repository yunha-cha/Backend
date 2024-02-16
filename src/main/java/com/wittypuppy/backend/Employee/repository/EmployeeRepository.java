package com.wittypuppy.backend.Employee.repository;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Login_EmployeeRepository")
public interface EmployeeRepository extends JpaRepository<LoginEmployee, Integer> {

    LoginEmployee findByEmployeeId(String employeeId);  // 회원아이디로 조회

    LoginEmployee findByEmployeeEmail(String employeeEmail);

    Optional<LoginEmployee> findByEmployeeIdAndEmployeeEmail(String employeeId, String employeeEmail); //비밀번호 찾기용


}
