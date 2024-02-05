package com.wittypuppy.backend.messenger.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class DepartmentDTO {
    private Long departmentCode;

    private String departmentName;
}
