package com.wittypuppy.backend.calendar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "CALENDAR_EVENT_OPTIONS")
@Table(name = "tbl_event_options")
public class EventOptions {
    @Id
    @Column(name = "event_options_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventOptionsCode;

    @Column(name = "event_code")
    private Long eventCode;

    @Column(name = "event_title", columnDefinition = "VARCHAR(100)")
    private String eventTitle;

    @Column(name = "event_content", columnDefinition = "VARCHAR(3000)")
    private String eventContent;

    @Column(name = "event_start_date", columnDefinition = "DATETIME")
    private Date eventStartDate;

    @Column(name = "event_end_date", columnDefinition = "DATETIME")
    private Date eventEndDate;

    @Column(name = "event_location", columnDefinition = "VARCHAR(500)")
    private String eventLocation;

    @Column(name = "event_recurrence_rule", columnDefinition = "VARCHAR(1000)")
    private String eventRecurrenceRule;

    @Column(name = "event_delete_time", columnDefinition = "DATETIME")
    private Date eventDeleteTime;

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

    @Column(name = "event_isallday")
    private String eventIsAllDay;

    @Column(name = "event_category")
    private String eventCategory;

    public EventOptions setEventOptionsCode(Long eventOptionsCode) {
        this.eventOptionsCode = eventOptionsCode;
        return this;
    }

    public EventOptions setEventCode(Long eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public EventOptions setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
        return this;
    }

    public EventOptions setEventContent(String eventContent) {
        this.eventContent = eventContent;
        return this;
    }

    public EventOptions setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
        return this;
    }

    public EventOptions setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
        return this;
    }

    public EventOptions setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
        return this;
    }

    public EventOptions setEventRecurrenceRule(String eventRecurrenceRule) {
        this.eventRecurrenceRule = eventRecurrenceRule;
        return this;
    }

    public EventOptions setEventDeleteTime(Date eventDeleteTime) {
        this.eventDeleteTime = eventDeleteTime;
        return this;
    }

    public EventOptions setEventDeleteStatus(String eventDeleteStatus) {
        this.eventDeleteStatus = eventDeleteStatus;
        return this;
    }

    public EventOptions setEventEditable(String eventEditable) {
        this.eventEditable = eventEditable;
        return this;
    }

    public EventOptions setEventColor(String eventColor) {
        this.eventColor = eventColor;
        return this;
    }

    public EventOptions setEventBackgroundColor(String eventBackgroundColor) {
        this.eventBackgroundColor = eventBackgroundColor;
        return this;
    }

    public EventOptions setEventDragBackgroundColor(String eventDragBackgroundColor) {
        this.eventDragBackgroundColor = eventDragBackgroundColor;
        return this;
    }

    public EventOptions setEventBorderColor(String eventBorderColor) {
        this.eventBorderColor = eventBorderColor;
        return this;
    }

    public EventOptions setEventIsAllDay(String eventIsAllDay) {
        this.eventIsAllDay = eventIsAllDay;
        return this;
    }

    public EventOptions setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
        return this;
    }

    public EventOptions builder() {
        return new EventOptions(eventOptionsCode, eventCode, eventTitle, eventContent, eventStartDate, eventEndDate, eventLocation, eventRecurrenceRule, eventDeleteTime, eventDeleteStatus, eventEditable, eventColor, eventBackgroundColor, eventDragBackgroundColor, eventBorderColor, eventIsAllDay, eventCategory);
    }
}
