package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.exception.DataDeletionException;
import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.project.dto.*;
import com.wittypuppy.backend.project.entity.*;
import com.wittypuppy.backend.project.exception.invalidProjectMemberException;
import com.wittypuppy.backend.project.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
    public List<ProjectMainInterface> selectProjectListWithPaging(Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<ProjectMainInterface> projectMainDTOList = projectRepository.findAllProjectInfoWithPaging(index * count, count);
        return projectMainDTOList;
    }

    /* 내 프로젝트 목록 확인 */
    public List<ProjectMainInterface> selectMyProjectListWithPaging(Long userEmployeeCode, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<ProjectMainInterface> projectMainDTOList = projectRepository.findMyProjectInfoWithPaging(userEmployeeCode, index * count, count);
        return projectMainDTOList;
    }

    /* 내 부서 프로젝트 목록 확인 */
    public List<ProjectMainInterface> selectMyDeptProjectListWithPaging(Long userEmployeeCode, Criteria cri) {
        Employee employee = employeeRepository.findById(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 정보를 찾을 수 없습니다."));
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<ProjectMainInterface> projectMainDTOList = projectRepository.findMyDeptProjectInfoWithPaging(employee.getDepartment().getDepartmentCode(), index * count, count);
        return projectMainDTOList;
    }

    /* 프로젝트 검색하기 */
    public List<ProjectMainInterface> searchProjectListWithPaging(String searchValue, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<ProjectMainInterface> projectMainDTOList = projectRepository.searchAllProjectInfoWithPaging(searchValue, index * count, count);
        return projectMainDTOList;
    }

    /* 내 프로젝트 검색하기 */
    public List<ProjectMainInterface> searchMyProjectListWithPaging(Long userEmployeeCode, String searchValue, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<ProjectMainInterface> projectMainDTOList = projectRepository.searchMyProjectInfoWithPaging(userEmployeeCode, searchValue, index * count, count);
        return projectMainDTOList;
    }

    /* 내 부서 프로젝트 검색하기 */
    public List<ProjectMainInterface> searchMyDeptProjectListWithPaging(Long userEmployeeCode, String searchValue, Criteria cri) {
        Employee employee = employeeRepository.findById(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 정보를 찾을 수 없습니다."));
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<ProjectMainInterface> projectMainDTOList = projectRepository.searchMyDeptProjectInfoWithPaging(employee.getDepartment().getDepartmentCode(), searchValue, index * count, count);
        return projectMainDTOList;
    }

    /* 프로젝트 만들기 */
    @Transactional
    public String createProject(ProjectDTO projectDTO, Long userEmployeeCode) {
        Employee employee = employeeRepository.findById(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("로그인한 계정의 정보를 찾을 수 없습니다."));
        try {
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
        } catch (Exception e) {
            throw new DataInsertionException("프로젝트 생성 실패");
        }
        return "프로젝트 생성 성공";
    }

    /* 프로젝트 열기 */
    public Map<String, Object> openProject(Long projectCode, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다"));
        List<Long> projectMemberEmployeeCodeList = project.getProjectMemberList().stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode())
                .toList();
        Long projectAdminEmployeeCode = project.getProjectManager().getEmployeeCode();

        boolean isLocked = project.getProjectLockedStatus().equals("Y");
        boolean isMember = List.of(projectMemberEmployeeCodeList, projectAdminEmployeeCode).contains(userEmployeeCode);
        if (isLocked && !isMember) {
            throw new invalidProjectMemberException("허가된 사원이 아닙니다.");
        }
        Map<String, Object> resultMap = new HashMap<>();
        ProjectDTO projectDTO = new ProjectDTO()
                .setProjectCode(project.getProjectCode())
                .setProjectTitle(project.getProjectTitle())
                .setProjectDescription(project.getProjectDescription())
                .setProjectDeadline(project.getProjectDeadline())
                .setProjectLockedStatus(project.getProjectLockedStatus())
                .builder();
        resultMap.put("project", projectDTO);
        Employee projectManager = project.getProjectManager();
        EmployeeDTO projectManagerDTO = modelMapper.map(projectManager, EmployeeDTO.class);
        resultMap.put("projectManager", projectManagerDTO);
        List<ProjectMember> projectMemberList = projectMemberRepository.findAllByProjectCodeAndProjectMemberDeleteStatus(projectCode, "N");
        List<ProjectMemberDTO> projectMemberDTOList = projectMemberList.stream().map(projectMember -> modelMapper.map(projectMember, ProjectMemberDTO.class)).toList();
        resultMap.put("projectMemberList", projectMemberDTOList);

        return resultMap;
    }

    /* 프로젝트를 열게 되는데. 거기서 게시글들을 따로 읽어온다.*/
    public List<ProjectPostDTO> selectProjectPostListWithPaging(Long projectCode, Criteria cri, Long userEmployeeCode) {
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", userEmployeeCode)
                .orElse(null); // 현재 프로젝트의 멤버가 아니면 null
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (projectMember == null && project.getProjectLockedStatus().equals("Y")) {
            throw new invalidProjectMemberException("허가된 사원이 아닙니다.");
        }
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<ProjectPostDTO> projectPostList = projectPostRepository.selectProjectPostListWithPaging(projectCode, index * count, count);
        return projectPostList;
    }

    /* 프로젝트 수정 */
    @Transactional
    public String modifyProject(Long projectCode, ProjectOptionsDTO projectOptionsDTO, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return "해당 프로젝트의 관리자가 아닙니다.";
        }
        try {
            project = project.setProjectTitle(projectOptionsDTO.getProjectTitle())
                    .setProjectDescription(projectOptionsDTO.getProjectDescription())
                    .setProjectProgressStatus(projectOptionsDTO.getProgressStatus())
                    .setProjectDeadline(projectOptionsDTO.getProjectDeadline())
                    .setProjectLockedStatus(projectOptionsDTO.getProjectLockedStatus())
                    .builder();
        } catch (Exception e) {
            throw new DataUpdateException("프로젝트 수정 실패");
        }
        return "프로젝트 수정 성공";
    }

    /* 프로젝트 초대를 위한 사원 목록 가져오기 */
    public List<EmployeeDTO> selectEmployeeList() {
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .toList();
        return employeeDTOList;
    }

    /* 프로젝트 멤버 초대하기 */
    @Transactional
    public String inviteProjectMembers(Long projectCode, List<Long> inviteEmployeeCodeList, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return "해당 프로젝트의 관리자가 아닙니다.";
        }
        List<ProjectMember> projectMemberList = projectMemberRepository.findAllByProjectCodeAndProjectMemberDeleteStatus(projectCode, "N");
        List<Long> projectMemberEmployeeCodeList = projectMemberList.stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode())
                .collect(Collectors.toList());
        inviteEmployeeCodeList.removeAll(projectMemberEmployeeCodeList);
        if (inviteEmployeeCodeList.size() <= 0) {
            throw new DataInsertionException("사원 정보가 이미 프로젝트에 존재합니다.");
        }

        try {
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
        } catch (Exception e) {
            throw new DataInsertionException("프로젝트 멤버 초대 실패");
        }
        return "프로젝트 멤버 초대 성공";
    }

    /* 프로젝트 멤버에서 나가기 */
    @Transactional
    public String exitProjectMember(Long projectCode, Long userEmployeeCode) {
        try {
            Project project = projectRepository.findById(projectCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
            if (project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
                return "관리자를 위임해야 나갈 수 있습니다.";
            }
            ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("프로젝트 멤버에 현재 로그인한 계정이 존재하지 않습니다."));
            projectMember.setProjectMemberDeleteStatus("Y");
        } catch (Exception e) {
            throw new DataDeletionException("프로젝트에서 나가기 실패");
        }
        return "프로젝트에서 나가기 성공";
    }

    /* 프로젝트 멤버 내보내기*/
    @Transactional
    public String kickProjectMember(Long projectCode, Long kickedProjectMemberCode, Long userEmployeeCode) {
        try {
            Project project = projectRepository.findById(projectCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
            if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
                return "현재 계정이 관리자가 아닙니다.";
            }
            ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndProjectMemberCode(projectCode, "N", kickedProjectMemberCode)
                    .orElseThrow(() -> new DataNotFoundException("프로젝트 멤버에 현재 강퇴할 계정이 존재하지 않습니다."));
            System.out.println("projectMember = " + projectMember);
            if (projectMember.getEmployee().getEmployeeCode().equals(userEmployeeCode)) {
                return "자기자신을 내보낼 수 없습니다.";
            }
            projectMember.setProjectMemberDeleteStatus("Y");
        } catch (Exception e) {
            throw new DataDeletionException("멤버 내보내기 실패");
        }
        return "멤버 내보내기 성공";
    }

    /* 프로젝트 게시글 만들기 */
    @Transactional
    public String createProjectPost(ProjectPostDTO projectPostDTO, Long userEmployeeCode) {
        LocalDateTime now = LocalDateTime.now();
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectPostDTO.getProjectCode(), "N", userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정은 프로젝트 멤버가 아닙니다."));
        try {
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
                    .setProjectPostCreationDate(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setProjectPostDueDate(projectPostDTO.getProjectPostDueDate())
                    .setProjectPostMemberList(Collections.singletonList(projectPostMember))
                    .builder();
            projectPostRepository.save(projectPost);
        } catch (Exception e) {
            throw new DataInsertionException("프로젝트 게시글 생성 실패");
        }
        return "프로젝트 게시글 생성 성공";
    }

    /* 프로젝트 게시글 열기 */
    public Map<String, Object> openProjectPost(Long projectPostCode, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("현재 프로젝트 게시글 정보가 존재하지 않습니다."));
        ProjectPostMainDTO projectPostMainDTO = new ProjectPostMainDTO()
                .setProjectPostCode(projectPost.getProjectPostCode())
                .setProjectCode(projectPost.getProjectCode())
                .setProjectPostStatus(projectPost.getProjectPostStatus())
                .setProjectPostPriority(projectPost.getProjectPostPriority())
                .setProjectPostTitle(projectPost.getProjectPostTitle())
                .setProjectPostCreationDate(projectPost.getProjectPostCreationDate())
                .setProjectPostModifyDate(projectPost.getProjectPostModifyDate())
                .setProjectPostDueDate(projectPost.getProjectPostDueDate())
                .builder();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("projectPostInfo", projectPostMainDTO);
        List<ProjectPostMemberDTO> projectPostMemberList = projectPostMemberRepository.selectProjectPostMember(projectPostCode);
        resultMap.put("projectPostMemberList", projectPostMemberList);

        return resultMap;
    }

    /* 프로젝트 게시글 댓글 읽기*/
    public List<ProjectPostCommentDTO> selectProjectPostCommentList(Long projectPostCode, Long userEmployeeCode) {
        List<ProjectPostCommentDTO> projectPostCommentList = projectPostCommentRepository.selectProjectPostCommentList(projectPostCode);
        return projectPostCommentList;
    }

    /* 프로젝트 게시글 댓글 파일 읽기*/
    public List<ProjectPostCommentFileDTO> selectProjectPostCommentFile(Long projectPostCommentCode, Long userEmployeeCode) {
        List<ProjectPostCommentFileDTO> projectPostCommentFileList = projectPostCommentFileRepository.selectProjectPostCommentFileList(projectPostCommentCode);
        return projectPostCommentFileList;
    }

    /* 프로젝트 게시글 초대를 위한 프로젝트 멤버 목록 가져오기 */
    public List<ProjectMemberDTO> selectProjectMemberList(Long projectCode, Long userEmployeeCode) {
        List<ProjectMember> projectMemberList = projectMemberRepository.findAllByProjectCodeAndProjectMemberDeleteStatus(projectCode, "N");
        List<ProjectMemberDTO> projectMemberDTOList = projectMemberList.stream().map(projectMember -> modelMapper.map(projectMember, ProjectMemberDTO.class))
                .toList();
        return projectMemberDTOList;
    }

    /* 프로젝트 게시글 멤버 초대하기 */
    @Transactional
    public String inviteProjectPostMembers(List<Long> inviteProjectMemberCodeList, Long projectPostCode, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글을 찾을 수 없습니다."));
        Project project = projectRepository.findByProjectPostList_ProjectPostCodeAndProjectMemberList_Employee_EmployeeCode(projectPostCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
        List<ProjectMember> projectMemberList = project.getProjectMemberList();
        List<Long> projectMemberCodeList = projectMemberList.stream().map(projectMember -> projectMember.getProjectMemberCode())
                .toList();
        try {
            inviteProjectMemberCodeList.removeAll(projectMemberCodeList);
            inviteProjectMemberCodeList.forEach(inviteProjectMemberCode -> {
                ProjectPostMember projectPostMember = new ProjectPostMember()
                        .setProjectPostCode(projectPostCode)
                        .setProjectMemberCode(inviteProjectMemberCode)
                        .setProjectPostMemberDeleteStatus("N")
                        .builder();
                projectPostMemberRepository.save(projectPostMember);
            });
        } catch (Exception e) {
            throw new DataInsertionException("프로젝트 게시글 멤버 초대 실패");
        }
        return "프로젝트 게시글 멤버 초대 성공";
    }

    /* 프로젝트 게시글 멤버에서 내보내기 */
    @Transactional
    public String kickProjectPostMember(Long kickProjectMemberCode, Long projectPostCode, Long userEmployeeCode) {
        ProjectPost projectPost = projectPostRepository.findById(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트 게시글을 찾을 수 없습니다."));
        Project project = projectRepository.findByProjectPostList_ProjectPostCode(projectPostCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트를 찾을 수 없습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            return "해당 계정은 관리자가 아닙니다.";
        }
        ProjectPostMember projectPostMember = projectPostMemberRepository.findByProjectPostMemberCodeAndProjectPostMemberDeleteStatus(kickProjectMemberCode, "N")
                .orElseThrow(() -> new DataNotFoundException("강퇴할 계정이 해당 프로젝트 게시글에 존재하지 않습니다."));
        try {
            projectPostMember.setProjectPostMemberDeleteStatus("Y");
        } catch (Exception e) {
            throw new DataUpdateException("프로젝트 게시글 멤버 내보내기 실패");
        }
        return "프로젝트 게시글 멤버 내보내기 성공";
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
                .setProjectPostCommentCreationDate(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setProjectPostMemberCode(projectPostMember.getProjectPostMemberCode())
                .builder();
        projectPostCommentRepository.save(projectPostComment);
    }

    /* 프로젝트 게시글 댓글 첨부파일 작성하기 */
}
