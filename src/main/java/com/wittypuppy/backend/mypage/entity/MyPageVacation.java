package com.wittypuppy.backend.mypage.entity;

import com.wittypuppy.backend.attendance.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MyPage_VACATION")
@Table(name = "tbl_vacation")
public class MyPageVacation {
    @Id
    @Column(name = "vacation_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacationCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private MyPageEmp vacationEmployeeCode;

    @Column(name = "vacation_creation_date", columnDefinition = "DATETIME")
    private LocalDateTime vacationCreationDate;

    @Column(name = "vacation_expiration_date", columnDefinition = "DATETIME")
    private LocalDateTime vacationExpirationDate;

    @Column(name = "vacation_creation_reason", columnDefinition = "VARCHAR(500)")
    private String vacationCreationReason;

    @Column(name = "vacation_usage_date", columnDefinition = "DATETIME")
    private LocalDateTime vacationUsageDate;

    @Column(name = "vacation_used_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String vacationUsedStatus;

    @Column(name = "vacation_type" , columnDefinition = "VARCHAR(50) DEFAULT '연차'")
    private String vacationType;
}
