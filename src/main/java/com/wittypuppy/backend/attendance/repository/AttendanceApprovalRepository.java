package com.wittypuppy.backend.attendance.repository;


import com.wittypuppy.backend.approval.entity.AdditionalApprovalLine;
import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.OnLeave;
import com.wittypuppy.backend.attendance.entity.Overwork;
import com.wittypuppy.backend.attendance.entity.WorkType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.util.List;
import java.util.stream.Stream;

public interface AttendanceApprovalRepository extends JpaRepository<ApprovalLine, Long> {


    //내 신청 문서 반려
    @Query(value = "SELECT " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason " +
            "FROM tbl_approval_line A " +
            "LEFT JOIN tbl_approval_document B ON A.approval_document_code = B.approval_document_code " +
            "WHERE A.approval_process_status = '반려' " +
            "AND A.approval_document_code " +
            "IN (SELECT approval_document_code " +
            "FROM tbl_approval_document  " +
            "WHERE employee_code = :empCode) " +
            "AND A.approval_document_code is not null ",
            nativeQuery = true)
    Page<ApprovalLine> findByApprovalProcessStatusAndLineEmployeeCode_EmployeeCodeNative(Pageable pageable, int empCode);


//내 문서 승인
    @Query(value = "SELECT  " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason " +
            "FROM tbl_approval_line A " +
            "LEFT JOIN tbl_approval_document B ON A.approval_document_code = B.approval_document_code " +
            "WHERE A.approval_process_status = '결재' " +
            "AND A.approval_document_code IN (SELECT approval_document_code " +
            "FROM tbl_approval_document " +
            "WHERE employee_code = :employeeCode) " +
            "AND A.approval_process_order = (SELECT MAX(approval_process_order) " +
            "FROM tbl_approval_line " +
            "WHERE A.approval_document_code = approval_document_code) " +
            "AND A.approval_document_code is not null ",
            nativeQuery = true)
    Page<ApprovalLine> findMyDocumentPayment(int employeeCode, Pageable paging);


    //내 기안 문서
    @Query(value = "SELECT " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason " +
            "FROM tbl_approval_line A " +
            "WHERE A.approval_process_status = '기안' " +
            "AND A.employee_code = :employeeCode " +
            "AND A.approval_document_code is not null ",
            nativeQuery = true)
    Page<ApprovalLine> findByApplyDocument(int employeeCode, Pageable paging);




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
            "AND A.approval_process_status = '결재'" +
            "AND A.approval_document_code is not null "
            , nativeQuery = true)

    // 내가 결재한 문서
    Page<ApprovalLine> approvalPayment(Pageable paging, int employeeCode);



    //내가 결재할 문서 - 대기

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
            "WHERE A.approval_process_status = '대기' " +
            "AND A.employee_code = :employeeCode " +
            "AND A.approval_document_code is not null " +
            "AND A.approval_process_order IN (SELECT approval_process_order - 1 FROM tbl_approval_line) " +
            "AND ((A.approval_process_status IN ('결재'or '기안') " +
            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
            "                                           WHERE approval_process_status IN ('결재'or '기안'))) " +
            "    OR " +
            "    (A.approval_process_status IN ('대기'or '반려'or '회수') " +
            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
            "                                           WHERE approval_process_status IN ('대기'or '반려'or '회수'))))",
            nativeQuery = true)
    Page<ApprovalLine> paymentWaiting(Pageable paging, int employeeCode);





    @Query(value = "SELECT " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason " +
            "FROM tbl_approval_line A " +
            "LEFT JOIN tbl_approval_document B ON A.approval_document_code = B.approval_document_code " +
            "LEFT JOIN tbl_employee C ON B.employee_code = C.employee_code " +
            "LEFT JOIN tbl_department D ON C.department_code = D.department_code " +
            "WHERE A.approval_process_status = '대기' " +
            "AND A.employee_code = :employeeCode " +
            "AND A.approval_document_code is not null " +
            "AND A.approval_process_order IN (SELECT approval_process_order - 1 FROM tbl_approval_line) " +
            "AND ((A.approval_process_status IN ('결재'or '기안') " +
            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
            "                                           WHERE approval_process_status IN ('결재'or '기안'))) " +
            "    OR " +
            "    (A.approval_process_status IN ('대기'or '반려'or '회수') " +
            "      AND A.approval_process_order - 1 IN (SELECT approval_process_order FROM tbl_approval_line " +
            "                                           WHERE approval_process_status IN ('대기'or '반려'or '회수'))))",
            nativeQuery = true)
    //대기 수량으로 표현
    List<ApprovalLine> attendanceWaiting(int employeeCode);


    @Query(value = "SELECT " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason " +
            "FROM tbl_approval_line A " +
            "WHERE A.approval_document_code = :approvalDocumentCode ",
            nativeQuery = true)
    List<ApprovalLine> findByApprovalDocumentCode(Long approvalDocumentCode);


    @Query(value =
            "SELECT " +
                    "employee_code " +
                    "FROM tbl_approval_line " +
                    "WHERE approval_document_code = :approvalDocumentCode " +
                    "AND approval_process_status = '대기' " +
                    "ORDER BY approval_line_code ASC " +
                    "LIMIT 1",
            nativeQuery = true)
    int approvalSubjectEmployeeCode(Long approvalDocumentCode);



    @Query(value =
            "SELECT " +
                    "A.approval_document_code, "+
                    "A.approval_line_code, "+
                    "A.approval_process_date, "+
                    "A.approval_process_order, "+
                    "A.employee_code, "+
                    "A.approval_process_status, " +
                    "A.approval_rejected_reason "+
                    "FROM tbl_approval_line A " +
                    "WHERE A.approval_document_code = :approvalDocumentCode " +
                    "AND A.employee_code = :employeeCode " ,
            nativeQuery = true)
    ApprovalLine approvalList(Long approvalDocumentCode, int employeeCode);


}




