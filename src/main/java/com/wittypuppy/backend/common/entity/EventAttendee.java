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
@Table(name = "tbl_event_attendee")
public class EventAttendee {
    @Id
    @Column(name = "event_attendee_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventAttendeeCode;

    @Column(name="event_code", columnDefinition = "BIGINT")
    private Long eventCode;

    @Column(name="employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @JoinColumn(name = "event_attendee_code")
    @OneToMany
    private List<EventAlert> eventAlertList;
}
