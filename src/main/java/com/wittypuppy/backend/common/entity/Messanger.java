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
@Table(name = "tbl_messanger")
public class Messanger {
    @Id
    @Column(name = "messanger_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messangerCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "messanger_option", columnDefinition = "VARCHAR(100)")
    private String messangerOption;

    @Column(name = "messanger_mini_alram_option", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String messangerMiniAlramOption;

    @Column(name = "messanger_theme", columnDefinition = "VARCHAR(100) DEFAULT 'DEFAULT'")
    private String messangerTheme;

    @JoinColumn(name = "messanger_code")
    @OneToMany
    private List<Chatroom> chatroomList;
}
