package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_email")
public class Email {
    @Id
    @Column(name = "email_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailCode;

    @Column(name = "email_read_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String emailReadStatus;

    @Column(name = "email_title", columnDefinition = "VARCHAR(100)")
    private String emailTitle;

    @Column(name = "email_content", columnDefinition = "VARCHAR(3000)")
    private String emailContent;

    @Column(name = "email_send_time", columnDefinition = "DATETIME")
    private LocalDateTime emailSendTime;

    @Column(name = "email_reservation_time", columnDefinition = "DATETIME")
    private LocalDateTime emailReservationTime;

    @Column(name = "email_status", columnDefinition = "VARCHAR(100)")
    private String emailStatus;

    @ManyToOne
    @JoinColumn(name="email_receiver_employee_code", columnDefinition = "BIGINT")
    private Employee emailReceiverEmployee;

    @ManyToOne
    @JoinColumn(name = "email_sender_employee_code", columnDefinition = "BIGINT")
    private Employee emailSenderEmployee;
}
