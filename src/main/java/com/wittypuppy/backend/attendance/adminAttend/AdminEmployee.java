package com.wittypuppy.backend.attendance.adminAttend;

import com.wittypuppy.backend.attendance.entity.Department;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name ="ATTENDANCE_ADMIN_EMPLOYEE")
@Table(name = "tbl_employee")
public class AdminEmployee {
    @Id
    @Column(name = "employee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adminEmployeeCode;

    @JoinColumn(name = "department_code")
    @OneToOne
    private Department employeeAdminDepartmentCode;


    @Column(name = "employee_name", columnDefinition = "VARCHAR(100)")
    private String employeeNameAdmin;


    @Column(name = "employee_join_date", columnDefinition = "DATETIME")
    private LocalDateTime employeeJoinDateAdmin;


    @Column(name="employee_role", columnDefinition = "VARCHAR(10)")
    private String employeeRole;

    @Column(name = "vacation_creation_date")
    private LocalDateTime vacationCreationDate;

    @Column(name = "vacation_expiration_date")
    private LocalDateTime vacationExpirationDate;

    @Column(name = "vacation_creation_reason")
    private String vacationCreationReason;


}
