package com.wittypuppy.backend.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "CALENDAR_EVENT_ATTENDEE")
@Table(name = "tbl_event_attendee")
public class EventAttendee {
    @Id
    @Column(name = "event_attendee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventAttendeeCode;

    @Column(name="event_code")
    private Long eventCode;

    @JoinColumn(name="employee_code")
    @ManyToOne
    private Employee employee;

    @JoinColumn(name="event_attendee_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<EventAlert> eventAlertList;
}
