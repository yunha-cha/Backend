package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceLineRepository extends JpaRepository<ApprovalLine, Long> {
    Page<ApprovalLine> findByLineEmployeeCode_employeeCodeAndApprovalProcessStatus(Pageable paging, Long employeeCode, String 반려);







//        @Query(value = "SELECT COUNT(*) " +
//            "A.approval_document_code, " +
//            "A.approval_line_code, " +
//            "A.approval_process_date, " +
//            "A.approval_process_order, " +
//            "A.employee_code, " +
//            "A.approval_process_status, " +
//            "A.approval_rejected_reason " +
//            "FROM tbl_approval_line A " +
//            "LEFT JOIN tbl_approval_document B ON A.employee_code = B.employee_code " +
//            "WHERE A.approval_process_status = '대기' " +
//            "AND A.employee_code = :employeeCode " +
//            "AND A.approval_process_order IN (SELECT approval_process_order - 1 FROM tbl_approval_line) " +
//            "AND ((A.approval_process_status IN ('결재'or '기안') " +
//            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
//            "                                           WHERE approval_process_status IN ('결재'or '기안'))) " +
//            "    OR " +
//            "    (A.approval_process_status IN ('대기'or '반려'or '회수') " +
//            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
//            "                                           WHERE approval_process_status IN ('대기'or '반려'or '회수'))))",
//            nativeQuery = true)
//    ApprovalLine attendanceWaiting(Long employeeCode);

}
