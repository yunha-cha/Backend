package com.wittypuppy.backend.messenger.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ChatFileDTO {
    private String chatFileOgFile;
    private String chatFileChangedFile;
    private LocalDateTime chatFileUpdateDate;
}
