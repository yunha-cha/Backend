package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventOptionsDTO {
    private EventDTO event;
    private List<Long> eventAttendeeEmployeeCodeList;
    private EventAlertDTO eventAlert;
}
