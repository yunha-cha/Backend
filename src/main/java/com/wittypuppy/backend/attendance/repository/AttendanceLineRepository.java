package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.DetailMyWaing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AttendanceLineRepository extends JpaRepository<ApprovalLine, Long> {


    @Query(value = "SELECT " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason, " +
            "C.employee_name, " +
            "D.department_name " +
            "FROM tbl_approval_line A " +
            "LEFT JOIN tbl_approval_document B ON A.approval_document_code = B.approval_document_code " +
            "LEFT JOIN tbl_employee C ON B.employee_code = C.employee_code " +
            "LEFT JOIN tbl_department D ON C.department_code = D.department_code " +
            "WHERE A.employee_code = :employeeCode " +
            "AND A.approval_process_status = '반려'" +
            "AND A.approval_document_code is not null "
            , nativeQuery = true)
    //내가 반려한 문서
    Page<ApprovalLine> rejectionDocument(Pageable paging, int employeeCode);





    @Query(value = "SELECT " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason, " +
            "B.overwork_date, " +
            "B.overwork_end_time, " +
            "B.overwork_start_time, " +
            "B.kind_of_overwork, " +
            "B.overwork_reason, " +
            "B.overwork_title " +
            "FROM tbl_approval_line A " +
            "LEFT JOIN tbl_overwork B ON A.approval_document_code = B.approval_document_code " +
            "WHERE A.approval_document_code = :approvalDocumentCode " +
            "AND A.approval_process_order = (SELECT MAX(approval_process_order) " +
            "FROM tbl_approval_line " +
            "WHERE approval_document_code = :approvalDocumentCode)",
            nativeQuery = true)
    ApprovalLine findOver(Long approvalDocumentCode);







}
