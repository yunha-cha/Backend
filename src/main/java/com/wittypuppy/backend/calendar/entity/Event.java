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
@Entity(name = "CALENDAR_EVENT")
@Table(name = "tbl_event")
public class Event {
    @Id
    @Column(name = "event_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventCode;

    @Column(name = "calendar_code")
    private Long calendarCode;

    @JoinColumn(name = "department_code")
    @ManyToOne
    private Department department;

    @JoinColumn(name = "event_code")
    @OneToOne(cascade = CascadeType.ALL)
    private EventOptions eventOptions;


    public Event setEventCode(Long eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public Event setCalendarCode(Long calendarCode) {
        this.calendarCode = calendarCode;
        return this;
    }

    public Event setDepartment(Department department) {
        this.department = department;
        return this;
    }

    public Event setEventOptions(EventOptions eventOptions) {
        this.eventOptions = eventOptions;
        return this;
    }

    public Event builder() {
        return new Event(eventCode, calendarCode, department, eventOptions);
    }
}
