package com.wittypuppy.backend.project.dto.viewProjectInfo;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDTO {
    private Long employeeCode;

    private DepartmentDTO departmentDTO;

    private JobDTO jobDTO;

    private List<ProfileDTO> profileDTOList;
}
