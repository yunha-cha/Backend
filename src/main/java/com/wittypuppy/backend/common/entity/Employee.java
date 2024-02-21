package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_employee")
public class Employee {
    @Id
    @Column(name = "employee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @Column(name = "department_code", columnDefinition = "BIGINT")
    private Long departmentCode;

    @Column(name = "job_code", columnDefinition = "BIGINT")
    private Long jobCode;

    @Column(name = "employee_name", columnDefinition = "VARCHAR(100)")
    private String employeeName;

    @Column(name = "employee_birth_date", columnDefinition = "DATETIME")
    private LocalDateTime employeeBirthDate;

    @Column(name = "employee_resident_number", columnDefinition = "VARCHAR(50)")
    private String employeeResidentNumber;

    @Column(name = "employee_phone", columnDefinition = "VARCHAR(50)")
    private String employeePhone;

    @Column(name = "employee_address", columnDefinition = "VARCHAR(300)")
    private String employeeAddress;

    @Column(name = "employee_join_date", columnDefinition = "DATETIME")
    private LocalDateTime employeeJoinDate;

    @Column(name = "employee_retirement_date", columnDefinition = "DATETIME")
    private LocalDateTime employeeRetirementDate;

    @Column(name = "employee_id", columnDefinition = "VARCHAR(50)")
    private String employeeId;

    @Column(name = "employee_password", columnDefinition = "VARCHAR(50)")
    private String employeePassword;

    @Column(name="employee_assigned_code", columnDefinition = "BIGINT")
    private Long employeeAssignedCode;

    @Column(name="employee_on_leave_count", columnDefinition = "BIGINT")
    private Long employeeOnLeaveCount;

    @Column(name="employee_external_email", columnDefinition = "VARCHAR(100)")
    private String employeeExternalEmail;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<EventAttendee> eventAttendeeList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Calendar> calendarList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<ChatroomMember> chatroomMemberList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Messenger> messengerList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<AttendanceManagement> attendanceManagementList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<AttendanceWorkType> attendanceWorkTypeList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<ApprovalDoc> approvalDocList;

    @OneToMany(mappedBy = "approvalRepresentativeEmployee")
    private List<ApprovalRepresent> approvalRepresentativeList; /*manytoone 을 2번 걸어버리자!*/
    @OneToMany(mappedBy = "approvalAssigneeEmployee")
    private List<ApprovalRepresent> approvalAssigneeList; /*manytoone 을 2번 걸어버리자!*/

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Auth> authList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<BoardMember> boardMemberList;

    @JoinColumn(name = "board_manager_code")
    @OneToMany
    private List<Board> BoardList; /*게시판 관리자*/

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<PostLike> postLikeList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Post> postList;

    @OneToMany(mappedBy = "emailReceiverEmployee")
    private List<Email> emailReceiverList;
    @OneToMany(mappedBy = "emailSenderEmployee")
    private List<Email> emailSenderList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Vacation> vacationList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Education> educationList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Profile> profileList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<Career> careerList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<ApprovalComment> approvalCommentList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<ApprovalLine> approvalLineList;

    @JoinColumn(name = "employee_code")
    @OneToMany
    private List<ApprovalReference> approvalReferenceList;
}
