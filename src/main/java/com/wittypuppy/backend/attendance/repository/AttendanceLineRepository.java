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




}
