package com.wittypuppy.backend.mainpage.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class MainPageProjectListDTO {

    private Long projectCode;
    private Long projectPostCode;
    private String projectTitle;
    private String projectDescription;
    private String projectPostTitle;
    private LocalDateTime projectPostCreationDate;



















}
