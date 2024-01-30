package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.exception.DataDeletionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.project.dto.EmployeeDTO;
import com.wittypuppy.backend.project.dto.ProjectDTO;
import com.wittypuppy.backend.project.dto.ProjectMemberDTO;
import com.wittypuppy.backend.project.dto.ProjectPostDTO;
import com.wittypuppy.backend.project.entity.*;
import com.wittypuppy.backend.project.exception.CreateProjectException;
import com.wittypuppy.backend.project.exception.ModifyProjectException;
import com.wittypuppy.backend.project.exception.ProjectLockedException;
import com.wittypuppy.backend.project.repository.EmployeeRepository;
import com.wittypuppy.backend.project.repository.ProjectMemberRepository;
import com.wittypuppy.backend.project.repository.ProjectPostMemberRepository;
import com.wittypuppy.backend.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectPostRepository projectPostRepository;
    private final ProjectPostMemberRepository projectPostMemberRepository;

    public List<ProjectDTO> selectProjectListByTypeAndSearchValue(String type, String searchValue, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> start");
        List<Project> projectList = null;
        if (searchValue == null || searchValue.isBlank()) {
            if (type == null) {
                projectList = projectRepository.findAll();
            } else if (type.equals("me")) {
                projectList = projectRepository.findAllByProjectMemberList_Employee_EmployeeCode(employeeCode);
            } else if (type.equals("myteam")) {
                Long myDepartmentCode = employeeRepository.findById(employeeCode).orElseThrow(() -> new DataNotFoundException("사원 정보가 잘못 되었습니다."))
                        .getDepartment().getDepartmentCode();
                List<Long> employeeCodeList = employeeRepository.findAllByDepartment_DepartmentCode(myDepartmentCode).stream().map(Employee::getEmployeeCode).collect(Collectors.toList());
                projectList = projectRepository.findAllByProjectMemberList_Employee_EmployeeCodeIn(employeeCodeList);
            }
        } else {
            if (type == null) {
                projectList = projectRepository.findAllByProjectTitle(searchValue);
            } else if (type.equals("me")) {
                projectList = projectRepository.findAllByProjectTitleAndProjectMemberList_Employee_EmployeeCode(searchValue, employeeCode);
            } else if (type.equals("myteam")) {
                Long myDepartmentCode = employeeRepository.findById(employeeCode).orElseThrow(() -> new DataNotFoundException("사원 정보가 잘못 되었습니다."))
                        .getDepartment().getDepartmentCode();
                List<Long> employeeCodeList = employeeRepository.findAllByDepartment_DepartmentCode(myDepartmentCode).stream().map(Employee::getEmployeeCode).collect(Collectors.toList());
                projectList = projectRepository.findAllByProjectTitleAndProjectMemberList_Employee_EmployeeCodeIn(searchValue, employeeCodeList);
            }
        }

        List<ProjectDTO> projectDTOList = projectList.stream().map(project -> modelMapper.map(project, ProjectDTO.class)).collect(Collectors.toList());

        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> end");

        return projectDTOList;
    }

    @Transactional
    public String createProject(ProjectDTO projectDTO, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> start");

        int result;
        try {
            Employee employee = employeeRepository.findById(employeeCode).orElseThrow(() -> new DataNotFoundException("해당하는 사원 정보가 없습니다."));
            Project newProject = modelMapper.map(projectDTO, Project.class);
            newProject.setProjectManager(employee);
            ProjectMember projectMember = new ProjectMember();
            projectMember.setEmployee(employee);
            projectMember.setProjectMemberDeleteStatus("N");
            newProject.setProjectMemberList(Stream.of(projectMember).collect(Collectors.toList()));

            projectRepository.save(newProject);
            result = 1;
        } catch (Exception e) {
            throw new CreateProjectException("프로젝트 생성에 실패했습니다.");
        }

        log.info("[ProjectService] >>> selectProjectListByTypeAndSearchValue >>> end");
        return result > 0 ? "프로젝트 생성 성공" : "프로젝트 생성 실패";
    }

    public ProjectDTO selectProjectByProjectCode(Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> selectProjectByProjectCode >>> start");

        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당하는 프로젝트를 찾을 수 없습니다."));

        List<ProjectMember> projectMemberList = project.getProjectMemberList();

        boolean isLocked = project.getProjectLockedStatus().equals("Y");
        List<Long> employeeCodeList = projectMemberList.stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode()).toList();
        boolean isMember = employeeCodeList.contains(employeeCode);
        if (isLocked && !isMember) {
            throw new ProjectLockedException("허가되지 않은 사용자입니다.");
        }
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        log.info("[ProjectService] >>> selectProjectByProjectCode >>> end");
        return projectDTO;
    }

    @Transactional
    public String modifyProject(ProjectDTO projectDTO, Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> modifyProject >>> start");
        int result;

        try {
            Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
            project.setProjectTitle(projectDTO.getProjectTitle());
            project.setProjectDescription(projectDTO.getProjectDescription());
            project.setProjectProgressStatus(projectDTO.getProjectProgressStatus());
            project.setProjectDeadline(projectDTO.getProjectDeadline());
            project.setProjectLockedStatus(projectDTO.getProjectLockedStatus());
            result = 1;
        } catch (Exception e) {
            throw new ModifyProjectException("프로젝트 수정에 실패했습니다.");
        }


        log.info("[ProjectService] >>> modifyProject >>> end");

        return result > 0 ? "프로젝트 수정 성공" : "프로젝트 수정 실패";
    }

    @Transactional
    public String deleteProject(Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> deleteProject >>> start");
        int result;

        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
        if (project.getProjectManager().getEmployeeCode().equals(employeeCode)) {
            projectRepository.delete(project);
            result = 1;
        } else {
            throw new DataDeletionException("프로젝트 삭제에 실패했습니다.");
        }


        log.info("[ProjectService] >>> deleteProject >>> end");

        return result > 0 ? "프로젝트 삭제 성공" : "프로젝트 삭제 실패";
    }

    public List<EmployeeDTO> selectEmployeeList(Long employeeCode) {
        log.info("[ProjectService] >>> selectEmployeeList >>> start");

        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).toList();

        log.info("[ProjectService] >>> selectEmployeeList >>> end");
        return employeeDTOList;
    }

    @Transactional
    public String inviteProjectMembers(List<Long> employeeCodeList, Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> selectEmployeeList >>> start");
        int result = 0;

        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 없습니다."));
        if (project.getProjectManager().getEmployeeCode().equals(employeeCode)) {
            Set<Long> employeeCodeSet = new HashSet<>(employeeCodeList);
            Set<Long> originalEmployeeCodeSet = project.getProjectMemberList()
                    .stream()
                    .map(projectMember -> projectMember.getEmployee().getEmployeeCode())
                    .collect(Collectors.toSet());
            employeeCodeSet.removeAll(originalEmployeeCodeSet);
            employeeCodeSet.remove(employeeCode); // 혹시 모를 계정 정보도 삭제

            List<Employee> employeeList = employeeRepository.findAllById(employeeCodeSet);
            List<ProjectMember> projectMemberList = employeeList.stream()
                    .map(employee -> new ProjectMember(null, projectCode, employee, "N", null))
                    .collect(Collectors.toList());
            projectMemberRepository.saveAll(projectMemberList);
            result = 1;
        }

        log.info("[ProjectService] >>> selectEmployeeList >>> end");
        return result > 0 ? "프로젝트 삭제 성공" : "프로젝트 삭제 실패";
    }

    @Transactional
    public String kickOutProjectMember(Long badEmployeeCode, Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> kickOutProjectMember >>> start");
        int result = 0;
        /*
         * 1. 내가 관리자여야 한다.
         * 2. delete가 아니라 설정값을 바꿔야 한다.
         * */
        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당하는 프로젝트가 없습니다."));
        if (project.getProjectManager().getEmployeeCode().equals(employeeCode)) {
            ProjectMember badProjectMember =
                    projectMemberRepository.
                            findByProjectCodeAndEmployee_EmployeeCode(projectCode, badEmployeeCode).
                            orElseThrow(() -> new DataNotFoundException("강퇴할 회원이 해당 프로젝트에 없습니다."));
            badProjectMember.setProjectMemberDeleteStatus("Y");
            result = 1;
        }

        log.info("[ProjectService] >>> kickOutProjectMember >>> end");
        return result > 0 ? "프로젝트 강퇴 성공" : "프로젝트 강퇴 실패";
    }

    @Transactional
    public String exitProjectMember(Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> exitProjectMember >>> start");
        int result = 0;
        /*
         * 1. 내가 관리자이면 해당 나갈 수 없다.
         * */
        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당하는 프로젝트가 없습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(employeeCode)) {
            ProjectMember projectMember =
                    projectMemberRepository.
                            findByProjectCodeAndEmployee_EmployeeCode(projectCode, employeeCode).
                            orElseThrow(() -> new DataNotFoundException("내 정보가 해당 프로젝트에 없습니다."));
            projectMember.setProjectMemberDeleteStatus("Y");
            result = 1;
        }

        log.info("[ProjectService] >>> exitProjectMember >>> end");
        return result > 0 ? "프로젝트 나가기 성공" : "프로젝트 나가기 실패";
    }

    @Transactional
    public String delegateProject(Long projectCode, Long delegatedEmployeeCode, Long employeeCode) {
        log.info("[ProjectService] >>> delegateProject >>> start");
        int result = 0;
        /*
         * 1. 내가 관리자여야만 위임이 가능하다.
         * 2. 그 위임 대상자가 실제 멤버에 삭제여부가 N인 경우여야 한다.
         * */
        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당하는 프로젝트가 없습니다."));
        ProjectMember delegatedProjectMember =
                projectMemberRepository.
                        findByProjectCodeAndEmployee_EmployeeCodeAndProjectMemberDeleteStatus(projectCode, delegatedEmployeeCode, "N").
                        orElseThrow(() -> new DataNotFoundException("위임할 회원 정보가 없습니다."));
        if (project.getProjectManager().getEmployeeCode().equals(employeeCode) &&
                delegatedProjectMember != null) {
            // 내가 관리자이고 delegatedProjectMember 가 존재하면.
            project.setProjectManager(delegatedProjectMember.getEmployee());
            result = 1;
        }

        log.info("[ProjectService] >>> delegateProject >>> end");
        return result > 0 ? "프로젝트 관리자 위임 성공" : "프로젝트 관리자 위임 실패";
    }

    @Transactional
    public String createProjectPost(Long projectCode, ProjectPostDTO projectPostDTO, Long employeeCode) {
        log.info("[ProjectService] >>> createProjectPost >>> start");
        int result = 0;

        try {
            Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
            List<ProjectMember> projectMemberList = project.getProjectMemberList();
            List<Long> projectMemberCodeList = projectMemberList.stream().map(projectMember -> projectMember.getProjectMemberCode()).toList();
            if (projectMemberCodeList.contains(employeeCode)) {
                ProjectPost projectPost = modelMapper.map(projectPostDTO, ProjectPost.class);
                projectPost.setProjectCode(projectCode);
                projectPost.setProjectPostCreationDate(LocalDateTime.now());
                ProjectPostMember projectPostMember = new ProjectPostMember(null, null, employeeCode, "N", new ArrayList<>());
                projectPost.setProjectPostMemberList(Stream.of(projectPostMember).toList());

                projectPostRepository.save(projectPost);
                result = 1;
            }
        } catch (Exception e) {
        }


        log.info("[ProjectService] >>> createProjectPost >>> end");
        return result > 0 ? "프로젝트 게시글 생성 성공" : "프로젝트 게시글 생성 실패";
    }

    public ProjectPostDTO selectProjectPostByProjectPostCode(Long projectCode, Long projectPostCode, Long employeeCode) {
        log.info("[ProjectService] >>> createProjectPost >>> start");

        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 없습니다."));

        ProjectPost selectedProjectPost = null;
        try {
            selectedProjectPost
                    = project.getProjectPostList().stream().filter(projectPost -> projectPost.getProjectPostCode().equals(projectPostCode))
                    .toList().get(0);
        } catch (Exception e) {
            throw new DataNotFoundException("해당하는 프로젝트 게시글이 없습니다.");
        }
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndEmployee_EmployeeCode(projectCode, employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 접속자는 프로젝트 멤버가 아닙니다."));
        ProjectPostMember projectPostMember = projectPostMemberRepository.findByProjectPostCodeAndProjectMemberCode(projectPostCode, projectMember.getProjectMemberCode())
                .orElseThrow(() -> new DataNotFoundException("현재 접속자는 프로젝트 게시글 멤버가 아닙니다."));

        ProjectPostDTO projectPostDTO = modelMapper.map(selectedProjectPost, ProjectPostDTO.class);

        log.info("[ProjectService] >>> createProjectPost >>> end");

        return projectPostDTO;
    }

    @Transactional
    public String modifyProjectPost(Long projectCode, Long projectPostCode, ProjectPostDTO projectPostDTO, Long employeeCode) {
        log.info("[ProjectService] >>> modifyProjectPost >>> start");
        int result = 0;
        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (project.getProjectManager().getEmployeeCode().equals(employeeCode)) {
            ProjectPost projectPost = projectPostRepository.findById(projectPostCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글이 존재하지 않습니다."));
            projectPost.setProjectPostStatus(projectPostDTO.getProjectPostStatus());
            projectPost.setProjectPostPriority(projectPostDTO.getProjectPostStatus());
            projectPost.setProjectPostTitle(projectPostDTO.getProjectPostTitle());
            projectPost.setProjectPostModifyDate(LocalDateTime.now());
            projectPost.setProjectPostDueDate(projectPostDTO.getProjectPostDueDate());
        }
        /*
         * 댓글에 기록하는건 나중에
         * */

        log.info("[ProjectService] >>> modifyProjectPost >>> end");
        return result > 0 ? "프로젝트 게시글 수정 성공" : "프로젝트 게시글 수정 실패";
    }

    public List<ProjectMemberDTO> selectProjectMemberList(Long projectCode, Long employeeCode) {
        log.info("[ProjectService] >>> createProjectPost >>> start");

        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        List<ProjectMember> projectMemberList = project.getProjectMemberList().stream().filter(projectMember -> projectMember.getProjectMemberDeleteStatus().equals("N")).toList();

        List<ProjectMemberDTO> projectMemberDTOList = projectMemberList.stream().map(projectMember -> modelMapper.map(projectMember, ProjectMemberDTO.class)).toList();

        return projectMemberDTOList;
    }

    @Transactional
    public String inviteProjectPostMemberList(Long projectCode, Long projectPostCode, List<Long> projectMemberList, Long employeeCode) {
        log.info("[ProjectService] >>> inviteProjectPostMemberList >>> start");
        int result = 0;

        Project project = projectRepository.findById(projectCode).orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다"));
        try {
            if (project.getProjectManager().getEmployeeCode().equals(employeeCode)) {
                List<ProjectPostMember> projectPostMemberList = projectMemberList.stream().map(projectMember -> new ProjectPostMember(null, projectPostCode, projectMember, "N", null)).toList();
                projectPostMemberRepository.saveAll(projectPostMemberList);
            }
        } catch (Exception e) {

        }

        log.info("[ProjectService] >>> inviteProjectPostMemberList >>> end");
        return result > 0 ? "프로젝트 게시글 멤버 초대 성공" : "프로젝트 게시글 멤버 초대 실패";
    }
}
