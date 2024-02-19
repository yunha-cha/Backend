package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "MESSENGER_MESSENGER")
@Table(name = "tbl_messenger")
public class Messenger {
    @Id
    @Column(name = "messenger_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messengerCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "messenger_option")
    private String messengerOption; // 나중에 이름 수정 애매하다. 일단은 메신저 알람 위치

    @Column(name = "messenger_mini_alarm_option")
    private String messengerMiniAlarmOption;

    @Column(name = "messenger_theme")
    private String messengerTheme;

    public Messenger setMessengerCode(Long messengerCode) {
        this.messengerCode = messengerCode;
        return this;
    }

    public Messenger setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public Messenger setMessengerOption(String messengerOption) {
        this.messengerOption = messengerOption;
        return this;
    }

    public Messenger setMessengerMiniAlarmOption(String messengerMiniAlarmOption) {
        this.messengerMiniAlarmOption = messengerMiniAlarmOption;
        return this;
    }

    public Messenger setMessengerTheme(String messengerTheme) {
        this.messengerTheme = messengerTheme;
        return this;
    }

    public Messenger builder() {
        return new Messenger(messengerCode, employee, messengerOption, messengerMiniAlarmOption, messengerTheme);
    }
}
