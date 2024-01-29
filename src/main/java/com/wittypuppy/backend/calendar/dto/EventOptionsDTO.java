package com.wittypuppy.backend.calendar.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class EventOptionsDTO {
    private Long eventOptionsCode;

    private Long eventCode;

    private String eventTitle;

    private String eventContent;

    private LocalDateTime eventStartDate;

    private LocalDateTime eventEndDate;

    private String eventLocation;

    private String eventRecurrenceRule;

    private LocalDateTime eventDeleteTime;

    private String eventDeleteStatus;

    private String eventEditable;

    private String eventColor;

    private String eventBackgroundColor;

    private String eventDragBackgroundColor;

    private String eventBorderColor;
}
