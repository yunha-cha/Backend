package com.wittypuppy.backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponseDTO {
    private String code;
    private String description;
    private String detail;
}
