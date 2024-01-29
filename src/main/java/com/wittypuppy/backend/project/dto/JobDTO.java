package com.wittypuppy.backend.project.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class JobDTO {
    private Long jobCode;

    private String jobName;
}
