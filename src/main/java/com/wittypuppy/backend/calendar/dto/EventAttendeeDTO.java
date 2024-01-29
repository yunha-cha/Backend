package com.wittypuppy.backend.calendar.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EventAttendeeDTO {
    private Long eventAttendeeCode;

    private Long eventCode;

    private EmployeeDTO employeeDTO;

    private List<EventAlertDTO> eventAlertDTOList;
}
