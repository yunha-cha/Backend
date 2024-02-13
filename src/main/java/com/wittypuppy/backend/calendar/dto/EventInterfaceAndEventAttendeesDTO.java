package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventInterfaceAndEventAttendeesDTO {
    private EventInterface event;
    private List<EventAttendeeDTO> eventAttendeeList;
}
