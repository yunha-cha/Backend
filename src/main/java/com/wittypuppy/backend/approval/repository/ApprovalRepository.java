package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.approval.dto.EmployeeDTO;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<ApprovalDoc, Long> {
//    List<ApprovalDoc> findByemployeeCode(EmployeeDTO employeeDTO);
}
