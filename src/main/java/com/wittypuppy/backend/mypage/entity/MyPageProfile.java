package com.wittypuppy.backend.mypage.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "MYPAGE_PROFILE")
@Table(name = "tbl_profile")
public class MyPageProfile {
    @Id
    @Column(name = "profile_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileCode;

    @Column(name = "employee_code")
    private Long empCode;

    @Column(name = "profile_og_file")
    private String profileOgFile;

    @Column(name = "profile_changed_file")
    private String profileChangedFile;

    @Column(name = "profile_regist_date")
    private LocalDateTime profileRegistDate;

    @Column(name = "profile_delete_status")
    private String profileDeleteStatus;

    public MyPageProfile setProfileCode(Long profileCode) {
        this.profileCode = profileCode;
        return this;
    }

    public MyPageProfile setEmpCode(Long empCode) {
        this.empCode = empCode;
        return this;
    }

    public MyPageProfile setProfileOgFile(String profileOgFile) {
        this.profileOgFile = profileOgFile;
        return this;
    }

    public MyPageProfile setProfileChangedFile(String profileChangedFile) {
        this.profileChangedFile = profileChangedFile;
        return this;
    }

    public MyPageProfile setProfileRegistDate(LocalDateTime profileRegistDate) {
        this.profileRegistDate = profileRegistDate;
        return this;
    }

    public MyPageProfile setProfileDeleteStatus(String profileDeleteStatus) {
        this.profileDeleteStatus = profileDeleteStatus;
        return this;
    }

    public MyPageProfile builder() {
        return new MyPageProfile(profileCode, empCode, profileOgFile, profileChangedFile, profileRegistDate, profileDeleteStatus);
    }
}
