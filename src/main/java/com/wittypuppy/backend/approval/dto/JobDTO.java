package com.wittypuppy.backend.approval.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class JobDTO {
    private Long jobCode;
    private String jobName;
}
