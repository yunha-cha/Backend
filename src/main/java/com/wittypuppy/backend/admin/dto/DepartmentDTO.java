package com.wittypuppy.backend.admin.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
public class DepartmentDTO {
    private Long departmentCode;
    private Long parentDepartmentCode;
    private String departmentName;
}
