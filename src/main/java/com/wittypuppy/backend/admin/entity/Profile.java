package com.wittypuppy.backend.admin.entity;


import com.wittypuppy.backend.admin.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "ADMIN_PROFILE")
@Table(name = "tbl_profile")
public class Profile {
    @Id
    @Column(name = "profile_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileCode;

    @ManyToOne
    @JoinColumn(name = "employee_code")
    private Employee employee;

    @Column(name = "profile_og_file")
    private String profileOgFile;

    @Column(name = "profile_changed_file")
    private String profileChangedFile;

    @Column(name = "profile_regist_date")
    private LocalDateTime profileRegistDate;

    @Column(name = "profile_delete_status")
    private String profileDeleteStatus;
}

