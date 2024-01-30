package com.wittypuppy.backend.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "CALENDAR_CALENDAR")
@Table(name = "tbl_calendar")
public class Calendar {
    @Id
    @Column(name = "calendar_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "calendar_name")
    private String calendarName;

    @Column(name = "calendar_color")
    private String calendarColor;

    @Column(name = "calendar_background_color")
    private String calendarBackgroundColor;

    @Column(name = "calendar_drag_background_color")
    private String calendarDragBackgroundColor;

    @Column(name = "calendar_border_color")
    private String calendarBorderColor;

    @JoinColumn(name="calendar_code")
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Event> eventList;
}
