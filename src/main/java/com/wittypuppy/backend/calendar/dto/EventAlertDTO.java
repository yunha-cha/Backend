package com.wittypuppy.backend.calendar.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EventAlertDTO {
    private Long eventAlertCode;

    private Long eventAttendeeCode;

    private LocalDateTime eventAlertNotificationTime;

    private String eventAlertCheckStatus;
}
