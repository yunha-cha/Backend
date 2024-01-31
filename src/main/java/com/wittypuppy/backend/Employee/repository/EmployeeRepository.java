package com.wittypuppy.backend.Employee.repository;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<LoginEmployee, Integer> {

    LoginEmployee findByEmployeeId(String employeeId);  // 회원아이디로 조회

    LoginEmployee findByEmployeeEmail(String employeeEmail);

    /* purchase 도메인 추가하면서 추가한 메소드 */
//    @Query("SELECT a.memberCode FROM LoginEmployee a WHERE a.memberId = ?1")
//    int findMemberCodeByMemberId(String orderMemberId);

}
