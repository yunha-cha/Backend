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

    @OneToMany(mappedBy = "emailReceiver")
    private List<Email> emailReceiverList;
    @OneToMany(mappedBy = "emailSender")
    private List<Email> emailSenderList;
}
