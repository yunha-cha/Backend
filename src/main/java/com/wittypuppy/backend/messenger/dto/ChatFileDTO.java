package com.wittypuppy.backend.messenger.dto;


import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class ChatFileDTO {
    private String chatFileOgFile;
    private String chatFileChangedFile;
    private Date chatFileUpdateDate;
}
