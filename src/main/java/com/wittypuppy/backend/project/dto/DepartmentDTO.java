package com.wittypuppy.backend.project.dto;

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
