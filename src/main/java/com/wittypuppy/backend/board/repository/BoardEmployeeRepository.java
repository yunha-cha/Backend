package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardEmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByEmployeeCode(Long prKey);

}
