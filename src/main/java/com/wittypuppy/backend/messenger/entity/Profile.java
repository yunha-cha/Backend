package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "MESSENGER_PROFILE")
@Table(name = "tbl_profile")
public class Profile {
    @Id
    @Column(name = "profile_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileCode;

    @Column(name = "employee_code")
    private Long employeeCode;

    @Column(name = "profile_og_file")
    private String profileOgFile;

    @Column(name = "profile_changed_file")
    private String profileChangedFile;

    @Column(name = "profile_regist_date")
    private Date profileRegistDate;

    @Column(name = "profile_delete_status")
    private String profileDeleteStatus;
}
