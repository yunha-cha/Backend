package com.wittypuppy.backend.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class MessengerOptionsDTO {
    private String messengerPositionOption;
    private String messengerMiniAlarmOption;
    private String messengerTheme;

    public MessengerOptionsDTO setMessengerPositionOption(String messengerPositionOption) {
        this.messengerPositionOption = messengerPositionOption;
        return this;
    }

    public MessengerOptionsDTO setMessengerMiniAlarmOption(String messengerMiniAlarmOption) {
        this.messengerMiniAlarmOption = messengerMiniAlarmOption;
        return this;
    }

    public MessengerOptionsDTO setMessengerTheme(String messengerTheme) {
        this.messengerTheme = messengerTheme;
        return this;
    }

    public MessengerOptionsDTO builder() {
        return new MessengerOptionsDTO(messengerPositionOption, messengerMiniAlarmOption, messengerTheme);
    }
}
