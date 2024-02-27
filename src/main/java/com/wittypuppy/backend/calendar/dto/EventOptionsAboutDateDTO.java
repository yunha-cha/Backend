package com.wittypuppy.backend.calendar.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventOptionsAboutDateDTO {
    Date startDate;
    Date endDate;
    String isAllday;
}
