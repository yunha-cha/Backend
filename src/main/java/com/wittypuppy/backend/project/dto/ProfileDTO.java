package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ProfileDTO {
    private Long profileCode;

    private Long employeeCode;

    private String profileOgFile;

    private String profileChangedFile;

    private Date profileRegistDate;

    private String profileDeleteStatus;
}
