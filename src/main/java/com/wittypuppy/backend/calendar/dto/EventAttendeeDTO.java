package com.wittypuppy.backend.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class EventAttendeeDTO {
    private Long eventAttendeeCode;

    private Long eventCode;

    private EmployeeDTO employee;

    private EventAlertDTO eventAlert;

    public EventAttendeeDTO setEventAttendeeCode(Long eventAttendeeCode) {
        this.eventAttendeeCode = eventAttendeeCode;
        return this;
    }

    public EventAttendeeDTO setEventCode(Long eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public EventAttendeeDTO setEmployee(EmployeeDTO employee) {
        this.employee = employee;
        return this;
    }

    public EventAttendeeDTO setEventAlertList(EventAlertDTO eventAlert) {
        this.eventAlert = eventAlert;
        return this;
    }

    public EventAttendeeDTO builder() {
        return new EventAttendeeDTO(eventAttendeeCode, eventCode, employee, eventAlert);
    }
}
