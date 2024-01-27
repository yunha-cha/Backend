package com.wittypuppy.backend.project.dto.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DepartmentDTO {
    private Long departmentCode;

    private String departmentName;
}
