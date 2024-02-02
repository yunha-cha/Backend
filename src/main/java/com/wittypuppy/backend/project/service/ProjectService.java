package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.project.dto.ProjectDTO;
import com.wittypuppy.backend.project.dto.ProjectOptionsDTO;
import com.wittypuppy.backend.project.dto.ProjectPostCommentDTO;
import com.wittypuppy.backend.project.dto.ProjectPostDTO;
import com.wittypuppy.backend.project.entity.*;
import com.wittypuppy.backend.project.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final ProfileRepository profileRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectPostCommentFileRepository projectPostCommentFileRepository;
    private final ProjectPostCommentRepository projectPostCommentRepository;
    private final ProjectPostMemberRepository projectPostMemberRepository;
    private final ProjectPostRepository projectPostRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    /* 전체 프로젝트 목록 확인 */
    public void selectProjectList(Long userEmployeeCode) {
        List<Project> projectList = projectRepository.findAll();
    }

    /* 내 프로젝트 목록 확인 */
    public void selectMyProjectList(Long userEmployeeCode) {
        List<Project> projectList = projectRepository.findAllByProjectMemberList_Employee_EmployeeCode(userEmployeeCode);
    }

    /* 내 부서 프로젝트 목록 확인 */
    public void selectMyDeptProjectList(Long userEmployeeCode) {
        Employee employee = employeeRepository.findById(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("로그인한 계정의 정보를 찾을 수 없습니다."));
        List<Project> projectList = projectRepository.findAllByProjectManager_Department_DepartmentCode(employee.getDepartment().getDepartmentCode());
    }

    /* 프로젝트 검색하기 */
    public void searchProjectList(String searchValue) {
        List<Project> projectList = projectRepository.findAllByProjectTitleLike("%" + searchValue + "%");
    }

    /* 프로젝트 만들기 */
    @Transactional
    public void createProject(ProjectDTO projectDTO, Long userEmployeeCode) {
        Employee employee = employeeRepository.findById(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("로그인한 계정의 정보를 찾을 수 없습니다."));
        ProjectMember projectMember = new ProjectMember()
                .setEmployee(employee)
                .setProjectMemberDeleteStatus("N")
                .builder();

        Project project = new Project()
                .setProjectTitle(projectDTO.getProjectTitle())
                .setProjectManager(employee)
                .setProjectDescription(projectDTO.getProjectDescription())
                .setProjectDeadline(projectDTO.getProjectDeadline())
                .setProjectProgressStatus("프로젝트 생성")
                .setProjectLockedStatus(projectDTO.getProjectLockedStatus())
                .setProjectMemberList(Collections.singletonList(projectMember))
                .builder();

        projectRepository.save(project);
    }

    /* 프로젝트 열기 */
    public void openProject(Long projectCode, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다"));

        List<Long> projectMemberEmployeeCodeList = project.getProjectMemberList().stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode())
                .toList();
        Long projectAdminEmployeeCode = project.getProjectManager().getEmployeeCode();

        boolean isLocked = project.getProjectLockedStatus().equals("Y");
        boolean isMember = List.of(projectMemberEmployeeCodeList, projectAdminEmployeeCode).contains(userEmployeeCode);
        if (isLocked && !isMember) {
            return; // 열지 못하게
        }

        // Project 열기
    }

    /* 프로젝트 수정 */
    @Transactional
    public void modifyProject(ProjectOptionsDTO projectOptionsDTO, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectOptionsDTO.getProjectCode())
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return; // 해당 프로젝트의 관리자가 아닐 경우
        }
        project = project.setProjectTitle(projectOptionsDTO.getProjectTitle())
                .setProjectDescription(projectOptionsDTO.getProjectDescription())
                .setProjectProgressStatus(projectOptionsDTO.getProgressStatus())
                .setProjectDeadline(projectOptionsDTO.getProjectDeadline())
                .setProjectLockedStatus(projectOptionsDTO.getProjectLockedStatus())
                .builder();
    }

    /* 프로젝트 초대를 위한 사원 목록 가져오기 */
    public void selectEmployeeList() {
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();
    }

    /* 프로젝트 멤버 초대하기 */
    @Transactional
    public void inviteProjectMembers(List<Long> inviteEmployeeCodeList, Long projectCode, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return; // 해당 프로젝트의 관리자가 아닐 경우
        }
        List<ProjectMember> projectMemberList = projectMemberRepository.findAllByProjectCodeAndProjectMemberDeleteStatus(projectCode, "N");
        List<Long> projectMemberEmployeeCodeList = projectMemberList.stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode())
                .collect(Collectors.toList());
        inviteEmployeeCodeList.removeAll(projectMemberEmployeeCodeList);

        inviteEmployeeCodeList.forEach(inviteEmployeeCode -> {
            Employee newEmployee = employeeRepository.findById(inviteEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("추가할 사원 정보가 존재하지 않습니다."));
            ProjectMember projectMember = new ProjectMember()
                    .setProjectCode(projectCode)
                    .setEmployee(newEmployee)
                    .setProjectMemberDeleteStatus("N")
                    .builder();
            projectMemberRepository.save(projectMember);
        });
    }

    /* 프로젝트 멤버에서 나가기 */
    @Transactional
    public void exitProjectMember(Long projectCode, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return; // 해당 프로젝트의 관리자일 경우 나갈 수 없다.
        }
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("프로젝트 멤버에 현재 로그인한 계정이 존재하지 않습니다."));
        projectMember.setProjectMemberDeleteStatus("Y");
    }

    /* 프로젝트 멤버 내보내기*/
    @Transactional
    public void kickProjectMember(Long kickProjectMemberCode, Long projectCode, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return; // 해당 프로젝트의 관리자일 경우에만 강퇴처리 할 수 있다.
        }
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndProjectMemberCode(projectCode, "N", kickProjectMemberCode)
                .orElseThrow(() -> new DataNotFoundException("프로젝트 멤버에 현재 강퇴할 계정이 존재하지 않습니다."));
        if (projectMember.getEmployee().getEmployeeCode().equals(userEmployeeCode)) {
            return; // 자기자신은 강퇴할 수 없다.
        }
        projectMember.setProjectMemberDeleteStatus("Y");
    }

    /* 프로젝트 게시글 만들기 */
    @Transactional
    public void createProjectPost(ProjectPostDTO projectPostDTO, Long userEmployeeCode) {
        LocalDateTime now = LocalDateTime.now();
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectPostDTO.getProjectCode(), "N", userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정은 프로젝트 멤버가 아닙니다."));
        ProjectPostMember projectPostMember = new ProjectPostMember()
                .setProjectPostCode(projectPostDTO.getProjectCode())
                .setProjectMemberCode(projectMember.getProjectMemberCode())
                .setProjectPostMemberDeleteStatus("N")
                .builder();
        ProjectPost projectPost = new ProjectPost()
                .setProjectCode(projectPostDTO.getProjectCode())
                .setProjectPostStatus(projectPostDTO.getProjectPostStatus())
                .setProjectPostPriority(projectPostDTO.getProjectPostPriority())
                .setProjectPostTitle(projectPostDTO.getProjectPostTitle())
                .setProjectPostCreationDate(now)
                .setProjectPostDueDate(projectPostDTO.getProjectPostDueDate())
                .setProjectPostMemberList(Collections.singletonList(projectPostMember))
                .builder();

        projectPostRepository.save(projectPost);
    }

    /* 프로젝트 게시글 열기 */
    public void openProjectPost(Long projectPostCode, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정 정보가 존재하지 않습니다."));
    }

    /* 프로젝트 게시글 초대를 위한 프로젝트 멤버 목록 가져오기 */
    public void selectProjectMemberList(Long projectCode, Long userEmployeeCode) {
        List<ProjectMember> projectMemberList = projectMemberRepository.findAllByProjectCodeAndProjectMemberDeleteStatus(projectCode, "N");
    }

    /* 프로젝트 게시글 멤버 초대하기 */
    @Transactional
    public void inviteProjectPostMembers(List<Long> inviteProjectMemberCodeList, Long projectPostCode, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글을 찾을 수 없습니다."));
        Project project = projectRepository.findByProjectPostList_ProjectPostCodeAndProjectMemberList_Employee_EmployeeCode(projectPostCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
        List<ProjectMember> projectMemberList = project.getProjectMemberList();
        List<Long> projectMemberCodeList = projectMemberList.stream().map(projectMember -> projectMember.getProjectMemberCode())
                .toList();
        inviteProjectMemberCodeList.removeAll(projectMemberCodeList);
        inviteProjectMemberCodeList.forEach(inviteProjectMemberCode -> {
            ProjectPostMember projectPostMember = new ProjectPostMember()
                    .setProjectPostCode(projectPostCode)
                    .setProjectMemberCode(inviteProjectMemberCode)
                    .setProjectPostMemberDeleteStatus("N")
                    .builder();
            projectPostMemberRepository.save(projectPostMember);
        });
    }

    /* 프로젝트 게시글 멤버에서 내보내기 */
    @Transactional
    public void kickProjectPostMember(Long kickProjectMemberCode, Long projectPostCode, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글을 찾을 수 없습니다."));
        Project project = projectRepository.findByProjectPostList_ProjectPostCode(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return; // 관리자가 아니므로
        }
        ProjectPostMember projectPostMember = projectPostMemberRepository.findByProjectPostMemberCodeAndProjectPostMemberDeleteStatus(kickProjectMemberCode, "N")
                .orElseThrow(() -> new DataNotFoundException("강퇴할 계정이 해당 프로젝트 게시글에 존재하지 않습니다."));
        projectPostMember.setProjectPostMemberDeleteStatus("Y");
    }

    /* 프로젝트 게시글 멤버에서 나가기 */
    @Transactional
    public void exitProjectPostMember(Long projectPostCode, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글을 찾을 수 없습니다."));
        Project project = projectRepository.findByProjectPostList_ProjectPostCode(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
        ProjectPostMember projectPostMember = projectPostMemberRepository.findByProjectPostMemberCodeAndProjectPostMemberDeleteStatus(userEmployeeCode, "N")
                .orElseThrow(() -> new DataNotFoundException("현재 계정이 해당 프로젝트 게시글에 존재하지 않습니다."));
        projectPostMember.setProjectPostMemberDeleteStatus("Y");
    }

    /* 프로젝트 게시글 댓글 읽기 */
    public void selectProjectPostCommentList(Long projectPostCode, Long userEmployeeCode) {
        List<ProjectPostComment> projectPostCommentList = projectPostCommentRepository.findAllByProjectPostCode(projectPostCode);
    }

    /* 프로젝트 게시글 댓글 작성하기 */
    @Transactional
    public void createProjectPostComment(ProjectPostCommentDTO projectPostCommentDTO, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCommentDTO.getProjectPostCode())
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글이 없습니다."));
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectPost.getProjectCode(), "N", userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 멤버에 계정정보가 없습니다."));
        ProjectPostMember projectPostMember = projectPostMemberRepository.findByProjectPostCodeAndProjectMemberCodeAndProjectPostMemberDeleteStatus(projectPost.getProjectCode(), projectMember.getProjectMemberCode(), "N")
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글에 계정정보가 없습니다."));

        LocalDateTime now = LocalDateTime.now();
        ProjectPostComment projectPostComment = new ProjectPostComment()
                .setProjectPostCode(projectPostCommentDTO.getProjectPostCode())
                .setProjectPostCommentContent(projectPostCommentDTO.getProjectPostCommentContent())
                .setProjectPostCommentCreationDate(now)
                .setProjectPostMemberCode(projectPostMember.getProjectPostMemberCode())
                .builder();
        projectPostCommentRepository.save(projectPostComment);
    }

    /* 프로젝트 게시글 댓글 첨부파일 작성하기 */
}
