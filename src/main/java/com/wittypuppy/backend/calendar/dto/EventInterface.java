package com.wittypuppy.backend.calendar.dto;

import java.util.Date;

public interface EventInterface {
    Long getCalendarCode();

    Long getEventCode();

    String getDepartmentName();

    Long getEventAttendeeCount();

    String getEventTitle();

    String getEventContent();

    String getEventIsAllDay();

    Date getEventStartDate();

    Date getEventEndDate();

    String getEventLocation();

    String getEventRecurrenceRule();

    Date getEventDeleteTime();

    String getEventDeleteStatus();

    String getEventEditable();

    String getEventColor();

    String getEventBackgroundColor();

    String getEventDragBackgroundColor();

    String getEventBorderColor();

    String getEventCategory();
}