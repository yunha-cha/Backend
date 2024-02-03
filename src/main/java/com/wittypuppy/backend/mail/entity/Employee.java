package com.wittypuppy.backend.mail.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "MAIL_EMPLOYEE")
@Table(name = "tbl_employee")
public class Employee {
    @Id
    @Column(name = "employee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeCode;

    @Column(name = "employee_id")
    private String employeeId;

    //하나의 유저는 여러 이메일 테이블의 받는사람이 될 수 있다.
    @OneToMany(mappedBy = "emailReceiver")
    private List<Email> emailReceiverList;
    @OneToMany(mappedBy = "emailSender")
    private List<Email> emailSenderList;
}
