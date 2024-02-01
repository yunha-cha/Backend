package com.wittypuppy.backend.messenger.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MessengerOptionsDTO {
    private String messengerOption;
    private String messengerMiniAlarmOption;
    private String messengerTheme;
}
