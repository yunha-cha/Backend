package com.wittypuppy.backend.admin.dto;

import com.wittypuppy.backend.admin.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter@ToString
public class ProfileDTO {
    private Long profileCode;
    private EmployeeDTO employee;
    private String profileOgFile;
    private String profileChangedFile;
    private LocalDateTime profileRegistDate;
    private String profileDeleteStatus;
}
