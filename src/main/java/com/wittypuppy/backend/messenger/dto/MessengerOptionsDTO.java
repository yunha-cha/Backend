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
    private String messengerOption; // 나중에 이름 수정 애매하다. 일단은 메신저 알람 위치

    private String messengerMiniAlarmOption;

    private String messengerTheme;

    public MessengerOptionsDTO setMessengerOption(String messengerOption) {
        this.messengerOption = messengerOption;
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
        return new MessengerOptionsDTO(messengerOption, messengerMiniAlarmOption, messengerTheme);
    }
}
