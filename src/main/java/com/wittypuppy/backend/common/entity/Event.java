package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_event")
public class Event {
    @Id
    @Column(name = "event_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventCode;

    @Column(name = "calendar_code", columnDefinition = "BIGINT")
    private Long calendarCode;

    @Column(name = "department_code", columnDefinition = "BIGINT")
    private Long departmentCode;

    @JoinColumn(name = "event_code")
    @OneToMany
    private List<EventAttendee> eventAttendeeList;

    @JoinColumn(name="event_code")
    @OneToMany
    private List<EventOptions> eventOptionsList;
}
