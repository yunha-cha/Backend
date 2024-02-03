package com.wittypuppy.backend.messenger.dto;

import jakarta.persistence.*;
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

    private LocalDateTime profileRegistDate;

    private String profileDeleteStatus;
}
