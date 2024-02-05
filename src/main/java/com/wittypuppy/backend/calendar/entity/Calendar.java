package com.wittypuppy.backend.calendar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "CALENDAR_CALENDAR")
@Table(name = "tbl_calendar")
public class Calendar {
    @Id
    @Column(name = "calendar_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "calendar_name")
    private String calendarName;

    @Column(name = "calendar_color")
    private String calendarColor;

    @Column(name = "calendar_background_color")
    private String calendarBackgroundColor;

    @Column(name = "calendar_drag_background_color")
    private String calendarDragBackgroundColor;

    @Column(name = "calendar_border_color")
    private String calendarBorderColor;

    @JoinColumn(name = "calendar_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Event> eventList;

    public Calendar setCalendarCode(Long calendarCode) {
        this.calendarCode = calendarCode;
        return this;
    }

    public Calendar setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public Calendar setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    public Calendar setCalendarColor(String calendarColor) {
        this.calendarColor = calendarColor;
        return this;
    }

    public Calendar setCalendarBackgroundColor(String calendarBackgroundColor) {
        this.calendarBackgroundColor = calendarBackgroundColor;
        return this;
    }

    public Calendar setCalendarDragBackgroundColor(String calendarDragBackgroundColor) {
        this.calendarDragBackgroundColor = calendarDragBackgroundColor;
        return this;
    }

    public Calendar setCalendarBorderColor(String calendarBorderColor) {
        this.calendarBorderColor = calendarBorderColor;
        return this;
    }

    public Calendar setEventList(List<Event> eventList) {
        this.eventList = eventList;
        return this;
    }

    public Calendar builder() {
        return new Calendar(calendarCode, employee, calendarName, calendarColor, calendarBackgroundColor, calendarDragBackgroundColor, calendarBorderColor, eventList);
    }
}
