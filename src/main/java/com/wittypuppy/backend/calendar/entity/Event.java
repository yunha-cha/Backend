package com.wittypuppy.backend.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
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

    @JoinColumn(name = "event_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<EventAttendee> eventAttendeeList;
}
