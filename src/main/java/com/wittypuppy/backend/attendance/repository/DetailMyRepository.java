package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.DetailMyWaing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DetailMyRepository extends JpaRepository<DetailMyWaing, Long> {


    @Query(value = "SELECT DISTINCT " +
            "A.approval_document_code, " +
            "A.approval_line_code, " +
            "A.approval_process_date, " +
            "A.approval_process_order, " +
            "A.employee_code, " +
            "A.approval_process_status, " +
            "A.approval_rejected_reason, " +
            "B.work_type_end_date, " +
            "B.work_type_start_date, " +
            "B.work_type_form, " +
            "B.work_type_place, " +
            "B.work_type_reason, " +
            "B.work_type_title, " +
            "C.overwork_date, " +
            "C.overwork_end_time, " +
            "C.overwork_start_time, " +
            "C.kind_of_overwork, " +
            "C.overwork_reason, " +
            "C.overwork_title, " +
            "D.on_leave_end_date, " +
            "D.on_leave_start_date, " +
            "D.remaining_on_leave, " +
            "D.kind_of_on_leave, " +
            "D.on_leave_reason, " +
            "D.on_leave_title, " +
            "E.software_start_date, " +
            "E.kind_of_software, " +
            "E.software_reason, " +
            "E.software_title " +
            "FROM tbl_approval_line A " +
            "LEFT JOIN tbl_work_type B ON A.approval_document_code = B.approval_document_code " +
            "LEFT JOIN tbl_overwork C ON A.approval_document_code = C.approval_document_code " +
            "LEFT JOIN tbl_on_leave D ON A.approval_document_code = D.approval_document_code " +
            "LEFT JOIN tbl_software_use E ON A.approval_document_code = E.approval_document_code " +
            "WHERE A.approval_document_code = :approvalDocumentCode " +
            "AND A.approval_process_order = (SELECT MAX(approval_process_order) " +
                                            "FROM tbl_approval_line " +
                                            "WHERE approval_document_code = :approvalDocumentCode)",
            nativeQuery = true)
    DetailMyWaing findContent(Long approvalDocumentCode);

}
