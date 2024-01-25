package com.wittypuppy.backend.project.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.project.dto.ProjectAndProjectMemberDTO;
import com.wittypuppy.backend.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@Slf4j
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public ResponseEntity<ResponseDTO> selectProductList(String token/*, @RequestParam(name = "offset", defaultValue = "1") String offset*/) {
        log.info("[ProjectController] >>> selectProductList >>> start");
        // jwt토큰을 받을텐데. 이 값은 일련의 과정을 거쳐서 다음같이 employeeCode를 구할 수 있을 것이다.
        // Long employeeCode = Long.parseLong(token); // 여기서 일련의 과정을 했다고 치고.

        List<ProjectAndProjectMemberDTO> projectAndProjectMemberDTOList
                = projectService.selectProductList();

        log.info("[ProjectController] >>> selectProductList >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 리스트 조회 성공", projectAndProjectMemberDTOList));
    }
}
