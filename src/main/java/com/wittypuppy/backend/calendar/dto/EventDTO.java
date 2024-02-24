package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EventDTO {
    private Long eventCode;

    private Long calendarCode;

    private String departmentName;

    private Long eventAttendeeCount;

    private String eventTitle;

    private String eventContent;

    private String eventIsAllDay;

    private Date eventStartDate;

    private Date eventEndDate;

    private String eventLocation;

    private String eventRecurrenceRule;

    private Date eventDeleteTime;

    private String eventDeleteStatus;

    private String eventEditable;

    private String eventColor;

    private String eventBackgroundColor;

    private String eventDragBackgroundColor;

    private String eventBorderColor;

    private String eventCategory;
}
