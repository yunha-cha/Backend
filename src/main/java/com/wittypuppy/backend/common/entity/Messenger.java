package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_messenger")
public class Messenger {
    @Id
    @Column(name = "messenger_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messengerCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "messenger_option", columnDefinition = "VARCHAR(100)")
    private String messengerOption;

    @Column(name = "messenger_mini_alarm_option", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String messengerMiniAlramOption;

    @Column(name = "messenger_theme", columnDefinition = "VARCHAR(100) DEFAULT 'DEFAULT'")
    private String messengerTheme;
}
