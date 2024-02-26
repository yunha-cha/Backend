package com.wittypuppy.backend.mail.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "MAIL_EMAIL")
@Table(name = "tbl_email")
public class Email {
    @Id
    @Column(name = "email_code", columnDefinition = "BIGINT")   //이메일 코드
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailCode;

    @Column(name = "email_read_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")    //읽음 여부
    private String emailReadStatus;

    @Column(name = "email_title", columnDefinition = "VARCHAR(100)")    //이메일 제목
    private String emailTitle;  //받기

    @Column(name = "email_content", columnDefinition = "VARCHAR(3000)") //이메일 내용
    private String emailContent;    //HTML로 받기

    @Column(name = "email_send_time", columnDefinition = "DATETIME")    //이메일 보낸 시간
    private LocalDateTime emailSendTime;    //현재 시간으로

    @Column(name = "email_reservation_time", columnDefinition = "DATETIME") //예약한 시간
    private LocalDateTime emailReservationTime; //시간이 있으면 받기

    @Column(name = "email_status", columnDefinition = "VARCHAR(100)")   //이메일 상태(일반,임시보관,휴지통,예약한메일,중요한메일)
    private String emailStatus; //insert시 일반으로 update시 저거 중 하나로

//    @Column(name = "email_receiver_employee_code")
//    private Long emailReceiver;
//
//    @Column(name = "email_sender_employee_code")
//    private Long emailSender;


    @ManyToOne  //하나의 이메일에서
    @JoinColumn(name="email_receiver_employee_code", columnDefinition = "BIGINT")   //받는 이
    private Employee emailReceiver; //받기

    @ManyToOne
    @JoinColumn(name = "email_sender_employee_code", columnDefinition = "BIGINT")   //보낸 이
    private Employee emailSender;   //유저의 아이디 가져오기



//    @OneToMany(mappedBy = "emailCode")
//    private List<EmailAttachment> attachments;
}
