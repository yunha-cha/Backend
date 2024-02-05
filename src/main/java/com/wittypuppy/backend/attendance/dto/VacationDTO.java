package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.attendance.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VacationDTO {

    private Long vacationCode;

    private Employee vacationEmployeeCode;

    private LocalDateTime vacationCreationDate;

    private LocalDateTime vacationExpirationDate;

    private String vacationCreationReason;

    private LocalDateTime vacationUsageDate;

    private String vacationUsedStatus;

    private String vacationType;
}
