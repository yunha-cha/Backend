package com.wittypuppy.backend.project.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.dto.PageDTO;
import com.wittypuppy.backend.common.dto.PagingResponseDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.project.dto.EmployeeDTO;
import com.wittypuppy.backend.project.dto.ProjectDTO;
import com.wittypuppy.backend.project.dto.ProjectOptionsDTO;
import com.wittypuppy.backend.project.dto.ProjectPostDTO;
import com.wittypuppy.backend.project.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/project")
@AllArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;

    /**
     * 프로젝트를 조회한다.
     * <p>
     * 1. 전체 프로젝트 조회<br>
     * 2. 내 프로젝트 조회<br>
     * 3. 내 부서 프로젝트 조회
     * <p>
     * 제목을 통해 검색할 수도 있다. 위의 조회 타입과 합쳐서 사용 가능하다.
     *
     * @param projectType 프로젝트 타입 (all, me, myDept)
     * @param searchValue 제목 검색 값
     * @param principal   계정 정보
     * @return 200, 메시지, 보낼데이터 반환
     */
    @GetMapping("/projects")
    public ResponseEntity<ResponseDTO> selectProjectListWithPaging(@RequestParam(name = "type", required = false) String projectType,
                                                                   @RequestParam(name = "search", required = false) String searchValue,
                                                                   @RequestParam(name = "offset", defaultValue = "1") String offset,
                                                                   @AuthenticationPrincipal User principal) {
        Map<String, Object> result = null;
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        Criteria cri = new Criteria(Integer.valueOf(offset), 6);
        if (projectType.equals("all")) {
            if (Objects.isNull(searchValue) || searchValue.isBlank()) {
                result = projectService.selectProjectListWithPaging(userEmployeeCode, cri);
            } else {
                result = projectService.searchProjectListWithPaging(userEmployeeCode, searchValue, cri);
            }
        } else if (projectType.equals("me")) {
            if (Objects.isNull(searchValue) || searchValue.isBlank()) {
                result = projectService.selectMyProjectListWithPaging(userEmployeeCode, cri);
            } else {
                result = projectService.searchMyProjectListWithPaging(userEmployeeCode, searchValue, cri);
            }
        } else if (projectType.equals("myDept")) {
            if (Objects.isNull(searchValue) || searchValue.isBlank()) {
                result = projectService.selectMyDeptProjectListWithPaging(userEmployeeCode, cri);
            } else {
                result = projectService.searchMyDeptProjectListWithPaging(userEmployeeCode, searchValue, cri);
            }
        }
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(result.get("projectMainDTOList"));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, ((Long) result.get("projectListSize")).intValue()));
        return res("프로젝트 검색 성공", pagingResponseDTO);
    }

    /**
     * 프로젝트 정보를 통해 프로젝트를 생성
     * <p>
     * 1. 프로젝트 제목<br>
     * 2. 프로젝트 설명<br>
     * 3. 프로젝트 마감일<br>
     * 4. 프로젝트 잠금 여부<br>
     * <p>
     * 생성한 대상자가 프로젝트 관리자가 된다.
     *
     * @param project   입력한 프로젝트 정보 값
     * @param principal 계정 정보
     * @return 200, 메시지 반환
     */
    @PostMapping("/projects")
    public ResponseEntity<ResponseDTO> createProject(@RequestBody ProjectDTO project,
                                                     @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        Long result = projectService.createProject(project, userEmployeeCode);

        return res("프로젝트 생성 성공", result);
    }

    /**
     * 프로젝트에 접속한다.
     *
     * @param projectCode 전달받은 프로젝트 식별 코드 값
     * @param principal   계정 정보
     * @return 200, 메시지, 보낼데이터 반환
     */
    @GetMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> openProject(@PathVariable Long projectCode,
                                                   @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        Map<String, Object> result = projectService.openProject(projectCode, userEmployeeCode);
        return res("프로젝트 열기 성공", result);
    }

    @GetMapping("/projects/{projectCode}/paging")
    public ResponseEntity<ResponseDTO> selectProjectPostListWithPaging(
            @PathVariable Long projectCode,
            @RequestParam(name = "search", required = false, defaultValue = "") String searchValue,
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        Map<String, Object> result = null;
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);
        result = projectService.selectProjectPostListWithPaging("%" + searchValue + "%", projectCode, cri, userEmployeeCode);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(result.get("content"));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, ((Long) result.get("totalSize")).intValue()));
        return res("프로젝트 게시글 조회 성공", pagingResponseDTO);
    }

    /**
     * 프로젝트 코드와 프로젝트 정보 값을 전달하면 프로젝트를 수정한다.
     * <p>
     * 1. 프로젝트 제목<br>
     * 2. 프로젝트 설명<br>
     * 3. 프로젝트 진행 상태<br>
     * 4. 프로젝트 마감 기한<br>
     * 5. 프로젝트 잠금 여부
     *
     * @param projectCode    전달받은 프로젝트 식별 코드 값
     * @param projectOptions 입력한 프로젝트 정보 값
     * @param principal      계정 정보
     * @return 200, 메시지 반환
     */
    @PutMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> modifyProject(@PathVariable Long projectCode,
                                                     @RequestBody ProjectOptionsDTO projectOptions,
                                                     @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        String result = projectService.modifyProject(projectCode, projectOptions, userEmployeeCode);
        return res(result);
    }

    /**
     * 프로젝트 멤버로 초대하기 전에 사원 목록 출력을 위해 사원들의 정보를 가져온다.
     *
     * @param principal 계정 정보
     * @return 200, 메시지, 사원 리스트 반환
     */
    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> selectEmployeeList(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        List<EmployeeDTO> result = projectService.selectEmployeeList();
        return res("프로젝트에 사원 초대를 위한 사원 목록 조회", result);
    }

    /**
     * 사원을 프로젝트에 초대합니다. 관리자만 초대 가능합니다.
     * 이미 프로젝트에 존재하는 사원인 경우 자동으로 제외하고 초대한다.
     *
     * @param projectCode        해당 프로젝트 코드
     * @param inviteEmployeeCode 초대할 사원 코드
     * @param principal          계정 정보
     * @return 200, 메시지 반환
     */
    @PostMapping("/projects/{projectCode}/invite")
    public ResponseEntity<ResponseDTO> inviteProjectMembers(@PathVariable Long projectCode,
                                                            @RequestBody Long inviteEmployeeCode,
                                                            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("프로젝트 멤버 초대 성공", projectService.inviteProjectMember(projectCode, inviteEmployeeCode, userEmployeeCode));
    }

    /**
     * 프로젝트에서 나간다. 관리자인 경우 다른 사람에게 관리자를 위임해야 한다.
     *
     * @param projectCode 해당 프로젝트 식별 코드
     * @param principal   계정 정보
     * @return 200, 메시지 반환
     */
    @DeleteMapping("/projects/{projectCode}/leave")
    public ResponseEntity<ResponseDTO> leaveProjectMember(@PathVariable Long projectCode,
                                                          @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("프로젝트 나가기 성공", projectService.leaveProjectMember(projectCode, userEmployeeCode));
    }

    /**
     * 사원을 강제로 내보낸다. 프로젝트 관리자만 처리 가능하다.
     *
     * @param projectCode             해당 프로젝트 식별 코드
     * @param kickedProjectMemberCode 강제로 내보낼 사원의 프로젝트 멤버 코드
     * @param principal               계정 정보
     * @return 200, 메시지 반환
     */
    @DeleteMapping("/projects/{projectCode}/kick/{kickedProjectMemberCode}")
    public ResponseEntity<ResponseDTO> kickedProjectMember(@PathVariable Long projectCode,
                                                           @PathVariable Long kickedProjectMemberCode,
                                                           @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("프로젝트 멤버 내보내기 성공", projectService.kickedProjectMember(projectCode, kickedProjectMemberCode, userEmployeeCode));
    }

    @PutMapping("/projects/{projectCode}/delegate-admin")
    public ResponseEntity<ResponseDTO> delegateAdminOnProject(@PathVariable Long projectCode,
                                                              @RequestBody Long delegateEmployeeCode,
                                                              @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res(projectService.delegateAdminInProject(projectCode, delegateEmployeeCode, userEmployeeCode));
    }

    @PostMapping("/projects/upload-image")
    public ResponseEntity<ResponseDTO> uploadImage(
            MultipartFile file){
        return res("파일 업로드 성공", projectService.uploadImage(file));
    }

    @PostMapping("/projects/{projectCode}")
    public ResponseEntity<ResponseDTO> createProjectPost(
            @PathVariable Long projectCode,
            @RequestBody ProjectPostDTO projectPost,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("프로젝트 게시글 생성 성공", projectService.createProjectPost(projectCode, projectPost, userEmployeeCode));
    }


    /**
     * 정상적인 조회에 성공했을 경우 응답하는 메서드
     *
     * @param msg  메시지
     * @param data 보낼 데이터
     * @return 200, 메시지, 보낼데이터 로 응답
     */
    private ResponseEntity<ResponseDTO> res(String msg, Object data) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, msg, data));
    }

    /**
     * 정상적인 생성, 수정, 삭제에 성공할 경우 응답하는 메서드<br>
     * 반환할 데이터가 없고 이미 메시지에 어느정도 설명이 있으므로 이 Object를 생략한 값을 반환한다.
     *
     * @param msg 메시지
     * @return 200, 메시지 로 응답
     */
    private ResponseEntity<ResponseDTO> res(String msg) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, msg));
    }
}
