package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_event_options")
public class EventOptions {
    @Id
    @Column(name = "event_options_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventOptionsCode;

    @Column(name = "event_code", columnDefinition = "BIGINT")
    private Long eventCode;

    @Column(name = "event_title", columnDefinition = "VARCHAR(100)")
    private String eventTitle;

    @Column(name = "event_content", columnDefinition = "VARCHAR(3000)")
    private String eventContent;

    @Column(name = "event_start_date", columnDefinition = "DATETIME")
    private LocalDateTime eventStartDate;

    @Column(name = "event_end_date", columnDefinition = "DATETIME")
    private LocalDateTime eventEndDate;

    @Column(name = "event_location", columnDefinition = "VARCHAR(500)")
    private String eventLocation;

    @Column(name = "event_recurrence_rule", columnDefinition = "VARCHAR(1000)")
    private String eventRecurrenceRule;

    @Column(name = "event_delete_time", columnDefinition = "DATETIME")
    private LocalDateTime eventDeleteTime;

    @Column(name = "event_delete_status", columnDefinition = "VARCHAR(20) DEFAULT 'N'")
    private String eventDeleteStatus;

    @Column(name = "event_editable", columnDefinition = "VARCHAR(20) DEFAULT 'Y'")
    private String eventEditable;

    @Column(name = "event_color", columnDefinition = "VARCHAR(50)")
    private String eventColor;

    @Column(name = "event_background_color", columnDefinition = "VARCHAR(50)")
    private String eventBackgroundColor;

    @Column(name = "event_drag_background_color", columnDefinition = "VARCHAR(50)")
    private String eventDragBackgroundColor;

    @Column(name = "event_border_color", columnDefinition = "VARCHAR(100)")
    private String eventBorderColor;
}
