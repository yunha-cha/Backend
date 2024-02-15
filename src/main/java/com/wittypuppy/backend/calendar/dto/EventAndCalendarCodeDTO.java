package com.wittypuppy.backend.calendar.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventAndCalendarCodeDTO {
    private Long eventCode;
    private Long calendarCode;
    private String state;
}
