package com.wittypuppy.backend.calendar.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EventDTO {
    private Long eventCode;

    private Long calendarCode;

    private DepartmentDTO departmentDTO;

    private EventOptionsDTO eventOptionsDTO;

    private List<EventAttendeeDTO> eventAttendeeDTOList;
}
