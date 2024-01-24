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
@Table(name = "tbl_profile")
public class Profile {
    @Id
    @Column(name = "profile_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "profile_og_file", columnDefinition = "VARCHAR(500)")
    private String profileOgFile;

    @Column(name = "profile_changed_file", columnDefinition = "VARCHAR(500)")
    private String profileChangedFile;

    @Column(name = "profile_regist_date", columnDefinition = "DATETIME")
    private LocalDateTime profileRegistDate;

    @Column(name = "profile_delete_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String profileDeleteStatus;
}
