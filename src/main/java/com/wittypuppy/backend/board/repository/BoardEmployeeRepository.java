package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.Employee;
import com.wittypuppy.backend.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByEmployeeCode(Long prKey);

}
