package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EventAlertDTO {
    private Long eventAlertCode;

    private Long eventAttendeeCode;

    private Date eventAlertNotificationTime;

    private String eventAlertCheckStatus;
}
