package com.wittypuppy.backend.mypage.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class MyPageProfileDTO {


    private Long profileCode;

    private Long enpCode;

    private String profileOgFile;

    private String profileChangedFile;

    private LocalDateTime profileRegistDate;

    private String profileDeleteStatus;

}
