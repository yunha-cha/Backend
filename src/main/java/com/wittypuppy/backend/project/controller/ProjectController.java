package com.wittypuppy.backend.project.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.project.dto.EmployeeDTO;
import com.wittypuppy.backend.project.dto.ProjectDTO;
import com.wittypuppy.backend.project.dto.ProjectMemberDTO;
import com.wittypuppy.backend.project.dto.ProjectPostDTO;
import com.wittypuppy.backend.project.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@AllArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;

    /*전체 프로젝트 리스트*/
    /*내 프로젝트 리스트*/
    /*내 부서 프로젝트 리스트*/
    @GetMapping("/projects")
    public ResponseEntity<ResponseDTO> selectProjectListByType(
            @RequestParam(required = false) String type
    ) {
        log.info("[CalendarController] >>> selectEventByEventCode >>> start");
        Long employeeCode = 1L;

        List<ProjectDTO> projectListDTO = projectService.selectProjectListByTypeAndSearchValue(type, "", employeeCode);

        log.info("[CalendarController] >>> selectEventByEventCode >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 리스트 조회 성공", projectListDTO));
    }

    @GetMapping("/projects/search")
    public ResponseEntity<ResponseDTO> selectProjectListByTypeAndSearchValue(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String searchValue
    ) {
        log.info("[CalendarController] >>> selectProjectListByTypeAndSearchValue >>> start");
        Long employeeCode = 1L;

        List<ProjectDTO> projectDTOList = projectService.selectProjectListByTypeAndSearchValue(type, searchValue, employeeCode);

        log.info("[CalendarController] >>> selectProjectListByTypeAndSearchValue >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 리스트 검색 성공", projectDTOList));
    }

    @PostMapping("/projects")
    public ResponseEntity<ResponseDTO> createProject(
            @RequestBody ProjectDTO projectDTO
    ) {
        log.info("[CalendarController] >>> createProject >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.createProject(projectDTO, employeeCode);

        log.info("[CalendarController] >>> createProject >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 리스트 검색 성공", resultStr));
    }


    @GetMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> selectProjectByProjectCode(
            @PathVariable Long projectCode
    ) {
        log.info("[CalendarController] >>> selectProjectByProjectCode >>> start");
        Long employeeCode = 1L;

        ProjectDTO projectDTO = projectService.selectProjectByProjectCode(projectCode, employeeCode);

        log.info("[CalendarController] >>> selectProjectByProjectCode >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 접속 성공", projectDTO));
    }

    @PutMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> modifyProject(
            @RequestBody ProjectDTO projectDTO,
            @PathVariable Long projectCode
    ) {
        log.info("[CalendarController] >>> modifyProject >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.modifyProject(projectDTO, projectCode, employeeCode);

        log.info("[CalendarController] >>> selectProjectByProjectCode >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 수정 성공", resultStr));
    }

    @DeleteMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> deleteProject(
            @PathVariable Long projectCode
    ) {
        log.info("[CalendarController] >>> deleteProject >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.deleteProject(projectCode, employeeCode);

        log.info("[CalendarController] >>> deleteProject >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 삭제 성공", resultStr));
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> selectEmployeeList(
    ) {
        log.info("[CalendarController] >>> selectProjectByProjectCode >>> start");
        Long employeeCode = 1L;

        List<EmployeeDTO> employeeList = projectService.selectEmployeeList(employeeCode);

        log.info("[CalendarController] >>> selectProjectByProjectCode >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "사원 목록 가져오기 성공", employeeList));
    }

    @PostMapping("/projects/{projectCode}/invited")
    public ResponseEntity<ResponseDTO> inviteProjectMembers(
            @RequestBody List<Long> employeeCodeList,
            @PathVariable Long projectCode
    ) {
        log.info("[CalendarController] >>> deleteProject >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.inviteProjectMembers(employeeCodeList, projectCode, employeeCode);

        log.info("[CalendarController] >>> deleteProject >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 멤버 초대 성공", resultStr));
    }

    @PutMapping("/projects/{projectCode}/kickout/{badEmployeeCode}")
    public ResponseEntity<ResponseDTO> kickOutProjectMember(
            @PathVariable Long projectCode,
            @PathVariable Long badEmployeeCode
    ) {
        log.info("[CalendarController] >>> kickOutProjectMember >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.kickOutProjectMember(badEmployeeCode, projectCode, employeeCode);

        log.info("[CalendarController] >>> kickOutProjectMember >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 강퇴 성공", resultStr));
    }

    @PutMapping("/projects/{projectCode}/exit")
    public ResponseEntity<ResponseDTO> exitProjectMember(
            @PathVariable Long projectCode
    ) {
        log.info("[CalendarController] >>> exitProjectMember >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.exitProjectMember(projectCode, employeeCode);

        log.info("[CalendarController] >>> exitProjectMember >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 나가기 성공", resultStr));
    }

    @PutMapping("/projects/{projectCode}/delegate/{delegatedEmployeeCode}")
    public ResponseEntity<ResponseDTO> delegateProject(
            @PathVariable Long projectCode,
            @PathVariable Long delegatedEmployeeCode
    ) {
        log.info("[CalendarController] >>> delegateProject >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.delegateProject(projectCode, delegatedEmployeeCode, employeeCode);

        log.info("[CalendarController] >>> delegateProject >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 관리자 위임 성공", resultStr));
    }

    @PostMapping("/projects/{projectCode}/posts")
    public ResponseEntity<ResponseDTO> createProjectPost(
            @PathVariable Long projectCode,
            @RequestBody ProjectPostDTO projectPostDTO
    ) {
        log.info("[CalendarController] >>> createProjectPost >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.createProjectPost(projectCode, projectPostDTO, employeeCode);

        log.info("[CalendarController] >>> createProjectPost >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 게시글 생성 성공", resultStr));
    }

    @GetMapping("/projects/{projectCode}/posts/{projectPostCode}")
    public ResponseEntity<ResponseDTO> selectProjectPostByProjectPostCode(
            @PathVariable Long projectCode,
            @PathVariable Long projectPostCode
    ) {
        log.info("[CalendarController] >>> selectProjectPostByProjectPostCode >>> start");
        Long employeeCode = 1L;

        ProjectPostDTO projectPostDTO = projectService.selectProjectPostByProjectPostCode(projectCode, projectPostCode, employeeCode);

        log.info("[CalendarController] >>> selectProjectPostByProjectPostCode >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 게시글 입장 성공", projectPostDTO));
    }

    @PutMapping("/projects/{projectCode}/posts/{projectPostCode}")
    public ResponseEntity<ResponseDTO> modifyProjectPost(
            @PathVariable Long projectCode,
            @PathVariable Long projectPostCode,
            @RequestBody ProjectPostDTO projectPostDTO
    ) {
        log.info("[CalendarController] >>> modifyProjectPost >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.modifyProjectPost(projectCode, projectPostCode, projectPostDTO, employeeCode);

        log.info("[CalendarController] >>> modifyProjectPost >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 게시글 수정 성공", resultStr));
    }

    @GetMapping("/projects/{projectCode}/members")
    public ResponseEntity<ResponseDTO> selectProjectMemberList(
            @PathVariable Long projectCode
    ) {
        log.info("[CalendarController] >>> selectProjectMemberList >>> start");
        Long employeeCode = 1L;

        List<ProjectMemberDTO> projectMemberDTOList = projectService.selectProjectMemberList(projectCode, employeeCode);

        log.info("[CalendarController] >>> selectProjectMemberList >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 멤버 목록 조회 성공", projectMemberDTOList));
    }

    @PutMapping("/projects/{projectCode}/posts/{projectPostCode}/invited")
    public ResponseEntity<ResponseDTO> inviteProjectPostMemberList(
            @PathVariable Long projectCode,
            @PathVariable Long projectPostCode,
            @RequestBody List<Long> projectMemberList
    ) {
        log.info("[CalendarController] >>> inviteProjectPostMemberList >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.inviteProjectPostMemberList(projectCode, projectPostCode, projectMemberList, employeeCode);

        log.info("[CalendarController] >>> inviteProjectPostMemberList >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 게시글 멤버 초대 성공", resultStr));
    }

    @PutMapping("/projects/{projectCode}/posts/{projectPostCode}/kickout/{badProjectPostMemberCode}")
    public ResponseEntity<ResponseDTO> kickOutProjectPostMember(
            @PathVariable Long projectCode,
            @PathVariable Long projectPostCode,
            @PathVariable Long badProjectPostMemberCode
    ) {
        log.info("[CalendarController] >>> kickOutProjectPostMember >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.kickOutProjectPostMember(badProjectPostMemberCode, projectPostCode, projectCode, employeeCode);

        log.info("[CalendarController] >>> kickOutProjectPostMember >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 게시글 멤버 강퇴 성공", resultStr));
    }

    @PutMapping("/projects/{projectCode}/posts/{projectPostCode}/exit")
    public ResponseEntity<ResponseDTO> exitProjectPostMember(
            @PathVariable Long projectCode,
            @PathVariable Long projectPostCode
    ) {
        log.info("[CalendarController] >>> exitProjectPostMember >>> start");
        Long employeeCode = 1L;

        String resultStr = projectService.exitProjectPostMember(projectCode, projectPostCode, employeeCode);

        log.info("[CalendarController] >>> exitProjectPostMember >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "프로젝트 게시글 나가기 성공", resultStr));
    }
}
