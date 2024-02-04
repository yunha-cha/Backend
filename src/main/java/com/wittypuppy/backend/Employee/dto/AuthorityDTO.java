package com.wittypuppy.backend.Employee.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthorityDTO {

    private int authorityCode;
    private String authorityName;
    private String authorityDesc;
}
