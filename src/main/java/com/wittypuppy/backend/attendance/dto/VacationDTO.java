package com.wittypuppy.backend.attendance.dto;

import com.wittypuppy.backend.Employee.dto.User;
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

    private EmployeeDTO vacationEmployeeCode;

    private LocalDateTime vacationCreationDate;

    private LocalDateTime vacationExpirationDate;

    private String vacationCreationReason;

    private LocalDateTime vacationUsageDate;

    private String vacationUsedStatus;

    private String vacationType;

    private int total;  //연차 담는 객체

    private int useVacation; //사용한 연차 갯수 담는 객체

    private int useHalfVacation; //사용한 반차 갯수 담는 객체

    private double resultVacation; //연차 남은 갯수 담는 객체
}
