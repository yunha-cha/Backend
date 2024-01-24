package com.wittypuppy.backend.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Service
@ToString
public class DemoDTO {
    private Long demoCode;

    private String column1;

    private Long column2;

    private Double column3;

    private LocalDateTime column4;
}
