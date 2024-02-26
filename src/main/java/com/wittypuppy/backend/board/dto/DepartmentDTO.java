package com.wittypuppy.backend.board.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
public class DepartmentDTO {
    private Long departmentCode;
    private String departmentName;
    private Long parentDepartmentCode;

}
