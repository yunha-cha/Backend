package com.wittypuppy.backend.admin.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
public class CreateUserDTO {
    EmployeeDTO employee;
    CareerDTO career;
    EducationDTO education;
}
