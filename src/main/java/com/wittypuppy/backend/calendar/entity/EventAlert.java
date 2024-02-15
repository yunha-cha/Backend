package com.wittypuppy.backend.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "CALENDAR_EVENT_ALERT")
@Table(name = "tbl_event_alert")
public class EventAlert {
    @Id
    @Column(name = "event_alert_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventAlertCode;

    @Column(name = "event_attendee_code")
    private Long eventAttendeeCode;

    @Column(name = "event_alert_notification_time")
    private Date eventAlertNotificationTime;

    @Column(name = "event_alert_check_status")
    private String eventAlertCheckStatus;
}
