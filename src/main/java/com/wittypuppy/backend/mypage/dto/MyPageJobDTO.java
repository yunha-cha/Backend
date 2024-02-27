package com.wittypuppy.backend.mypage.dto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class MyPageJobDTO {
    private Long jobCode;

    private String jobName;

}
