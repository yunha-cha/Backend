package com.wittypuppy.backend.project.dto.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProfileDTO {
    private Long profileCode;

    private Long employeeCode;

    private String profileOgFile;

    private String profileChangedFile;

    private LocalDateTime profileRegistDate;

    private String profileDeleteStatus;
}
