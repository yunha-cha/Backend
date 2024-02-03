package com.wittypuppy.backend.attendance.dto;

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
