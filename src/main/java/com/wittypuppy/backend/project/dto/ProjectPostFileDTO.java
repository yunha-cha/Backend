package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectPostFileDTO {
    private Long projectPostFileCode;

    private String projectPostFileOgFile;

    private String projectPostFileChangedFile;

    private Date projectPostFileCreationDate;

    private Long projectPostCode;
}
