package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalDocRepository extends JpaRepository<ApprovalDoc, Long> {
    List<ApprovalDoc> findByEmployeeCode(LoginEmployee loginEmployee);
}
