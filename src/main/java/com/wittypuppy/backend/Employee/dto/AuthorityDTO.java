package com.wittypuppy.backend.Employee.dto;

import lombok.*;

import java.util.LinkedHashMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthorityDTO {

    private int authorityCode;
    private String authorityName;
//    private String authorityDesc;

    public static AuthorityDTO fromLinkedHashMap(LinkedHashMap<String, Object> map) {
        int authorityCode = (int) map.get("authorityCode");
        String authorityName = (String) map.get("authorityName");
        return new AuthorityDTO(authorityCode, authorityName);
    }
}
