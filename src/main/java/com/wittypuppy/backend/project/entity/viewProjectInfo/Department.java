package com.wittypuppy.backend.project.entity.viewProjectInfo;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name="PROJECT_VIEW_PROJECT_INFO_DEPARTMENT")
@Table(name = "tbl_department")
public class Department {
    @Id
    @Column(name = "department_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentCode;

    @Column(name = "department_name", columnDefinition = "VARCHAR(100)")
    private String departmentName;
}
