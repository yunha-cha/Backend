package com.wittypuppy.backend.mail.repository;

import com.wittypuppy.backend.mail.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailEmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findByEmployeeId(String emailReceiverEmployee);
}
