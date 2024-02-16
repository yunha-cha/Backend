package com.wittypuppy.backend.mail.repository;

import com.wittypuppy.backend.mail.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailEmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByEmployeeIdLike(String emailReceiverEmployee);


    Employee findByEmployeeIdLike(String emailReceiverEmployee);

    Employee findByEmployeeId(String user);
}
