package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.Employee.entity.LoginEmployee;
import com.wittypuppy.backend.approval.entity.AdditionalApprovalLine;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApprovalDocRepository extends JpaRepository<ApprovalDoc, Long> {
    Page<ApprovalDoc> findByEmployeeCode(LoginEmployee loginEmployee, Pageable pageable);

    @Query(value =
            "SELECT " +
                    "a.approval_document_code " +
                    "FROM tbl_approval_line a INNER JOIN tbl_approval_line b on a.approval_document_code = b.approval_document_code " +
                    "WHERE a.employee_code = :employeeCode " +
                    "AND a.approval_process_status = '기안' " +
                    "AND b.approval_process_status = '대기' " +
                    "AND a.approval_document_code = b.approval_document_code",
            nativeQuery = true)
    List<Long> outboxDocListOnprocess(Long employeeCode);

    @Query(value =
            "SELECT " +
                    "approval_document_code " +
            "FROM tbl_approval_line a " +
            "WHERE employee_code = :employeeCode " +
            "AND approval_process_status = '대기' " +
            "AND approval_process_order = (" +
            "SELECT " +
            "MIN(approval_process_order) " +
            "FROM tbl_approval_line b " +
            "WHERE " +
            "a.approval_document_code = b.approval_document_code " +
            "AND a.approval_process_status = b.approval_process_status)",
            nativeQuery = true)
    List<Long> inboxDocListByEmployeeCode(Long employeeCode);

    ApprovalDoc findByApprovalDocCode(Long approvalDocCode);

    List<ApprovalDoc> findByEmployeeCodeAndWhetherSavingApproval(LoginEmployee loginEmployee, String whetherSavingApproval);

    @Query(value =
            "SELECT approval_document_code " +
                    "FROM tbl_approval_line " +
            "WHERE employee_code = :employeeCode " +
            "AND approval_process_status not in ('기안', '회수', '대기')",
            nativeQuery = true)
    List<Long> inboxFinishedListbyEmployeeCode(Long employeeCode);
}
