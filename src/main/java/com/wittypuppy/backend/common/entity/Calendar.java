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
@Table(name = "tbl_calendar")
public class Calendar {
    @Id
    @Column(name = "calendar_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "calendar_name", columnDefinition = "VARCHAR(100)")
    private String calendarName;

    @Column(name = "calendar_color", columnDefinition = "VARCHAR(100)")
    private String calendarColor;

    @Column(name = "calendar_background_color", columnDefinition = "VARCHAR(100)")
    private String calendarBackgroundColor;

    @Column(name = "calendar_drag_background_color", columnDefinition = "VARCHAR(100)")
    private String calendarDragBackgroundColor;

    @Column(name = "calendar_border_color", columnDefinition = "VARCHAR(100)")
    private String calendarBorderColor;

    @JoinColumn(name = "calendar_code")
    @OneToMany
    private List<Event> eventList;
}
