package com.wittypuppy.backend.project.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.project.dto.ProjectAndProjectMemberDTO;
import com.wittypuppy.backend.project.dto.ProjectDTO;
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
    public ResponseEntity<ResponseDTO> selectProductList(
            /*, @RequestParam(name = "offset", defaultValue = "1") String offset*/
            @RequestParam(required = false) String condition // null또는"" & "me" & "myteam"
    ) {
        log.info("[ProjectController] >>> selectProductList >>> start");
        Long employeeCode = 1L; // 이거는 나중에 수정해야 한다.

        List<ProjectAndProjectMemberDTO> projectAndProjectMemberDTOList
                = projectService.selectProjectListByConditionAndSearchValue(condition, "", employeeCode);

        log.info("[ProjectController] >>> selectProductList >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 리스트 조회 성공", projectAndProjectMemberDTOList));
    }

    @GetMapping("/projects/search")
    public ResponseEntity<ResponseDTO> selectProductListBySearchValue(
            /*, @RequestParam(name = "offset", defaultValue = "1") String offset*/
            @RequestParam(required = false) String condition, // null또는""또는"   " & "me" & "myteam"
            @RequestParam(required = false) String searchValue // 검색결과.
    ) {
        log.info("[ProjectController] >>> selectProductListBySearchValue >>> start");
        Long employeeCode = 1L; // 이거는 나중에 수정해야 한다.

        List<ProjectAndProjectMemberDTO> projectAndProjectMemberDTOList
                = projectService.selectProjectListByConditionAndSearchValue(condition, searchValue, employeeCode);

        log.info("[ProjectController] >>> selectProductListBySearchValue >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 검색 결과 리스트 조회 성공", projectAndProjectMemberDTOList));
    }

    @PostMapping("/projects/create")
    public ResponseEntity<ResponseDTO> createNewProject(
            @RequestBody ProjectAndProjectMemberDTO projectAndProjectMemberDTO // 프로젝트 정보
    ) {
        log.info("[ProjectController] >>> createNewProject >>> start");
        Long employeeCode = 1L; // 이거는 나중에 수정해야 한다.

        projectAndProjectMemberDTO.setProjectManagerCode(employeeCode); // 생성한 사람이 프로젝트 관리자로

        String resultStr = projectService.createNewProject(projectAndProjectMemberDTO);

        log.info("[ProjectController] >>> createNewProject >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 추가 성공", resultStr));
    }

    @GetMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> selectProjectByProjectCode(
            @PathVariable Long projectCode
    ) {
        log.info("[ProjectController] >>> selectProjectByProjectCode >>> start");
        Long employeeCode = 1L; // 이거는 나중에 수정해야 한다.

        com.wittypuppy.backend.project.dto.viewProjectInfo.ProjectDTO projectDTO =
                projectService.selectProjectByProjectCode(projectCode, employeeCode);

        log.info("[ProjectController] >>> selectProjectByProjectCode >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 열기 성공", projectDTO));
    }

    @PutMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> modifyProject(
            @RequestBody ProjectDTO projectDTO,
            @PathVariable Long projectCode
    ) {
        log.info("[ProjectController] >>> modifyProject >>> start");
        Long employeeCode = 1L; // 이거는 나중에 수정해야 한다.

        String resultStr = projectService.modifyProject(projectDTO, projectCode, employeeCode);

        log.info("[ProjectController] >>> modifyProject >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 수정 성공", resultStr));
    }

    /*프로젝트 게시판에서 초대받지 못한 인원 확인하기*/

    /*프로젝트 멤버 초대하기*/

    /*프로젝트 멤버에서 나가기*/

    /*프로젝트 멤버 내보내기*/

    /*프로젝트 관리자 위임*/

    /*프로젝트 게시글 만들기*/

    /*프로젝트 게시글 열기*/

    /*프로젝트 게시글 멤버 초대*/

    /*프로젝트 게시글 멤버 내보내기*/

    /*프로젝트 게시글 멤버에서 나가기*/

    /*프로젝트 게시글 댓글 작성하기*/

    /*프로젝트 게시글 댓글 첨부파일 작성하기*/
}
