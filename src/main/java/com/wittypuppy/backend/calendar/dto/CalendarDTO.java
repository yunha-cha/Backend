package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class CalendarDTO {
    private Long calendarCode;

    private EmployeeDTO employeeDTO;

    private String calendarName;

    private String calendarColor;

    private String calendarBackgroundColor;

    private String calendarDragBackgroundColor;

    private String calendarBorderColor;

    private List<EventDTO> eventDTOList;
}
