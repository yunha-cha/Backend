package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_event_alert")
public class EventAlert {
    @Id
    @Column(name = "event_alert_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventAlertCode;

    @Column(name="event_attendee_code", columnDefinition = "BIGINT")
    private Long eventAttendeeCode;

    @Column(name="event_alert_notification_time", columnDefinition = "DATETIME")
    private LocalDateTime eventAlertNotificationTime;

    @Column(name="event_alert_check_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String eventAlertCheckStatus;
}
