package com.wittypuppy.backend.attendance.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SoftwareUseDTO {
    private Long softwareUseCode;
    private Long softDocCode;
    private String softwareTitle;
    private String kindOfSoftware;
    private String softwareReason;
    private LocalDateTime softwareStartDate;
}
