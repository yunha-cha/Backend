package com.wittypuppy.backend.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CalendarDTO {
    private Long calendarCode;

    private String calendarName;

    private String calendarColor;

    private String calendarBackgroundColor;

    private String calendarDragBackgroundColor;

    private String calendarBorderColor;


    public CalendarDTO setCalendarCode(Long calendarCode) {
        this.calendarCode = calendarCode;
        return this;
    }


    public CalendarDTO setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    public CalendarDTO setCalendarColor(String calendarColor) {
        this.calendarColor = calendarColor;
        return this;
    }

    public CalendarDTO setCalendarBackgroundColor(String calendarBackgroundColor) {
        this.calendarBackgroundColor = calendarBackgroundColor;
        return this;
    }

    public CalendarDTO setCalendarDragBackgroundColor(String calendarDragBackgroundColor) {
        this.calendarDragBackgroundColor = calendarDragBackgroundColor;
        return this;
    }

    public CalendarDTO setCalendarBorderColor(String calendarBorderColor) {
        this.calendarBorderColor = calendarBorderColor;
        return this;
    }

    public CalendarDTO builder() {
        return new CalendarDTO(calendarCode, calendarName, calendarColor, calendarBackgroundColor, calendarDragBackgroundColor, calendarBorderColor);
    }
}
