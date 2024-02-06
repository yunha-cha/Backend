package com.wittypuppy.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime profileRegistDate;

    private String profileDeleteStatus;
}
