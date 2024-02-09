package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApprovalDocRepository extends JpaRepository<ApprovalDoc, Long> {
    List<ApprovalDoc> findByEmployeeCode(LoginEmployee loginEmployee);

    @Query(value =
            "SELECT a FROM tbl_approval_document a " +
            "JOIN tbl_approval_line b on a.approval_document_code = b.approval_document_code " +
            "WHERE b.approval_process_status = '대기' " +
            "AND b.approval_process_order = (" +
            "SELECT MIN(b.approval_process_order) " +
            "WHERE a.approval_document_code = b.approval_document_code" +
            ")",
    nativeQuery = true)
    List<ApprovalDoc> findByReceiverEmployeeCode(LoginEmployee loginEmployee);
}
