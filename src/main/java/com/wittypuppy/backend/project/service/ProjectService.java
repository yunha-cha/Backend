package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.exception.DataDeletionException;
import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.project.dto.*;
import com.wittypuppy.backend.project.entity.Employee;
import com.wittypuppy.backend.project.entity.Project;
import com.wittypuppy.backend.project.entity.ProjectMember;
import com.wittypuppy.backend.project.entity.ProjectPost;
import com.wittypuppy.backend.project.exception.InvalidProjectMemberException;
import com.wittypuppy.backend.project.exception.NotProjectManagerException;
import com.wittypuppy.backend.project.exception.ProjectKickedException;
import com.wittypuppy.backend.project.exception.ProjectManagerException;
import com.wittypuppy.backend.project.repository.EmployeeRepository;
import com.wittypuppy.backend.project.repository.ProjectMemberRepository;
import com.wittypuppy.backend.project.repository.ProjectPostRepository;
import com.wittypuppy.backend.project.repository.ProjectRepository;
import com.wittypuppy.backend.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final EmployeeRepository employeeRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectPostRepository projectPostRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    public Map<String, Object> selectProjectListWithPaging(Long userEmployeeCode, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Long projectListSize = projectRepository.getCountAllProject();
        List<ProjectMainInterface> projectMainDTOList = projectRepository.findAllProjectInfoWithPaging(userEmployeeCode, index * count, count);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("projectListSize", projectListSize);
        returnMap.put("projectMainDTOList", projectMainDTOList);
        return returnMap;
    }

    public Map<String, Object> selectMyProjectListWithPaging(Long userEmployeeCode, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Long projectListSize = projectRepository.getCountMyProject(userEmployeeCode);
        List<ProjectMainInterface> projectMainDTOList = projectRepository.findMyProjectInfoWithPaging(userEmployeeCode, index * count, count);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("projectListSize", projectListSize);
        returnMap.put("projectMainDTOList", projectMainDTOList);
        return returnMap;
    }

    public Map<String, Object> selectMyDeptProjectListWithPaging(Long userEmployeeCode, Criteria cri) {
        Employee employee = employeeRepository.findByEmployeeCodeAndEmployeeRetirementDateIsNull(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 정보를 찾을 수 없습니다."));
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Long projectListSize = projectRepository.getCountMyDeptProject(employee.getDepartment().getDepartmentCode(), userEmployeeCode);
        List<ProjectMainInterface> projectMainDTOList = projectRepository.findMyDeptProjectInfoWithPaging(employee.getDepartment().getDepartmentCode(), userEmployeeCode, index * count, count);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("projectListSize", projectListSize);
        returnMap.put("projectMainDTOList", projectMainDTOList);
        return returnMap;
    }

    public Map<String, Object> searchProjectListWithPaging(Long userEmployeeCode, String searchValue, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Long projectListSize = projectRepository.getSearchCountAllProject(searchValue);
        List<ProjectMainInterface> projectMainDTOList = projectRepository.searchAllProjectInfoWithPaging(userEmployeeCode, searchValue, index * count, count);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("projectListSize", projectListSize);
        returnMap.put("projectMainDTOList", projectMainDTOList);
        return returnMap;
    }

    public Map<String, Object> searchMyProjectListWithPaging(Long userEmployeeCode, String searchValue, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Long projectListSize = projectRepository.getSearchCountMyProject(userEmployeeCode, searchValue);
        List<ProjectMainInterface> projectMainDTOList = projectRepository.searchMyProjectInfoWithPaging(userEmployeeCode, searchValue, index * count, count);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("projectListSize", projectListSize);
        returnMap.put("projectMainDTOList", projectMainDTOList);
        return returnMap;
    }

    public Map<String, Object> searchMyDeptProjectListWithPaging(Long userEmployeeCode, String searchValue, Criteria cri) {
        Employee employee = employeeRepository.findByEmployeeCodeAndEmployeeRetirementDateIsNull(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 정보를 찾을 수 없습니다."));
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Long projectListSize = projectRepository.getSearchCountMyDeptProject(employee.getDepartment().getDepartmentCode(), userEmployeeCode, searchValue);
        List<ProjectMainInterface> projectMainDTOList = projectRepository.searchMyDeptProjectInfoWithPaging(employee.getDepartment().getDepartmentCode(), userEmployeeCode, searchValue, index * count, count);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("projectListSize", projectListSize);
        returnMap.put("projectMainDTOList", projectMainDTOList);
        return returnMap;
    }

    @Transactional
    public Long createProject(ProjectDTO projectDTO, Long userEmployeeCode) {
        Employee employee = employeeRepository.findByEmployeeCodeAndEmployeeRetirementDateIsNull(userEmployeeCode)
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
                    .setProjectProgressStatus(projectDTO.getProjectProgressStatus())
                    .setProjectLockedStatus(projectDTO.getProjectLockedStatus())
                    .setProjectMemberList(Collections.singletonList(projectMember))
                    .builder();

            projectRepository.save(project);

            Long resultProjectCode = project.getProjectCode();
            return resultProjectCode;
        } catch (Exception e) {
            throw new DataInsertionException("프로젝트 생성 실패");
        }
    }

    public Map<String, Object> openProject(Long projectCode, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다"));
        List<Long> projectMemberEmployeeCodeList = project.getProjectMemberList().stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode())
                .toList();

        boolean isLocked = project.getProjectLockedStatus().equals("Y");
        boolean isMember = projectMemberEmployeeCodeList.contains(userEmployeeCode);
        if (isLocked && !isMember) {
            throw new InvalidProjectMemberException("허가된 사원이 아닙니다.");
        }
        Map<String, Object> resultMap = new HashMap<>();
        ProjectDTO projectDTO = new ProjectDTO()
                .setProjectCode(project.getProjectCode())
                .setProjectTitle(project.getProjectTitle())
                .setProjectDescription(project.getProjectDescription())
                .setProjectDeadline(project.getProjectDeadline())
                .setProjectLockedStatus(project.getProjectLockedStatus())
                .setProjectProgressStatus(project.getProjectProgressStatus())
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

    public Map<String, Object> selectProjectPostListWithPaging(String searchValue, Long projectCode, Criteria cri, Long userEmployeeCode) {
        ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", userEmployeeCode)
                .orElse(null); // 현재 프로젝트의 멤버가 아니면 null
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        boolean isLocked = project.getProjectLockedStatus().equals("Y");

        if (projectMember == null && isLocked) {
            throw new InvalidProjectMemberException("허가된 사원이 아닙니다.");
        }
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();

        PageRequest pageRequest = PageRequest.of(index, count);
        Page<ProjectPost> projectPostPage = projectPostRepository.findAllByProjectPostContentLikeAndProjectCodeOrderByProjectPostCreationDateDesc(searchValue, projectCode, pageRequest);
        List<ProjectPostDTO> projectPostList = projectPostPage
                .getContent()
                .stream()
                .map(projectPost -> modelMapper.map(projectPost, ProjectPostDTO.class))
                .collect(Collectors.toList());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("totalSize", projectPostPage.getTotalElements());
        returnMap.put("content", projectPostList);
        return returnMap;
    }

    @Transactional
    public String modifyProject(Long projectCode, ProjectOptionsDTO projectOptions, Long userEmployeeCode) {
        Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
        if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
            throw new NotProjectManagerException("해당 프로젝트의 관리자가 아닙니다.");
        }
        try {
            project = project.setProjectTitle(projectOptions.getProjectTitle())
                    .setProjectDescription(projectOptions.getProjectDescription())
                    .setProjectProgressStatus(projectOptions.getProjectProgressStatus())
                    .setProjectDeadline(projectOptions.getProjectDeadline())
                    .setProjectLockedStatus(projectOptions.getProjectLockedStatus())
                    .builder();
            return "프로젝트 수정 성공";
        } catch (Exception e) {
            throw new DataUpdateException("프로젝트 수정 실패");
        }
    }

    public List<EmployeeDTO> selectEmployeeList() {
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .toList();
        return employeeDTOList;
    }

    @Transactional
    public ProjectPostDTO inviteProjectMember(Long projectCode, Long inviteEmployeeCode, Long userEmployeeCode) {
        try {
            Project project = projectRepository.findById(projectCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
            if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
                throw new NotProjectManagerException("해당 프로젝트의 관리자가 아닙니다.");
            }
            List<ProjectMember> projectMemberList = projectMemberRepository.findAllByProjectCodeAndProjectMemberDeleteStatus(projectCode, "N");
            List<Long> projectMemberEmployeeCodeList = projectMemberList.stream().map(projectMember -> projectMember.getEmployee().getEmployeeCode())
                    .collect(Collectors.toList());
            if (projectMemberEmployeeCodeList.contains(inviteEmployeeCode)) {
                throw new InvalidProjectMemberException("해당 프로젝트에 이미 존재하는 사원입니다.");
            }

            Employee newEmployee = employeeRepository.findByEmployeeCodeAndEmployeeRetirementDateIsNull(inviteEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("추가할 사원 정보가 존재하지 않습니다."));
            ProjectMember newProjectMember = new ProjectMember()
                    .setProjectCode(projectCode)
                    .setEmployee(newEmployee)
                    .setProjectMemberDeleteStatus("N")
                    .builder();
            projectMemberRepository.save(newProjectMember);
            ProjectPostDTO projectMemberDTO = modelMapper.map(newProjectMember, ProjectPostDTO.class);
            return projectMemberDTO;
        } catch (Exception e) {
            throw new DataInsertionException("프로젝트 멤버 초대 실패");
        }
    }

    @Transactional
    public ProjectMemberDTO leaveProjectMember(Long projectCode, Long userEmployeeCode) {
        try {
            Project project = projectRepository.findById(projectCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
            if (project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
                throw new ProjectManagerException("관리자를 위임해야 나갈 수 있습니다.");
            }
            ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("프로젝트 멤버에 현재 로그인한 계정이 존재하지 않습니다."));
            projectMember.setProjectMemberDeleteStatus("Y");
            projectMemberRepository.save(projectMember);

            ProjectMemberDTO projectMemberDTO = modelMapper.map(projectMember, ProjectMemberDTO.class);
            return projectMemberDTO;
        } catch (Exception e) {
            throw new DataDeletionException("프로젝트에서 나가기 실패");
        }
    }

    @Transactional
    public ProjectMemberDTO kickedProjectMember(Long projectCode, Long kickedEmployeeCode, Long userEmployeeCode) {
        try {
            Project project = projectRepository.findById(projectCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
            if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
                throw new ProjectManagerException("현재 계정이 관리자가 아닙니다.");
            }
            ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", kickedEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("프로젝트 멤버에 현재 강퇴할 계정이 존재하지 않습니다."));
            if (projectMember.getEmployee().getEmployeeCode().equals(userEmployeeCode)) {
                throw new ProjectKickedException("자기자신을 내보낼 수 없습니다.");
            }
            projectMember.setProjectMemberDeleteStatus("Y");
            projectMemberRepository.save(projectMember);
            ProjectMemberDTO projectMemberDTO = modelMapper.map(projectMember, ProjectMemberDTO.class);
            return projectMemberDTO;
        } catch (Exception e) {
            throw new DataDeletionException("멤버 내보내기 실패");
        }
    }

    public ProjectPostFileDTO uploadImage(MultipartFile file) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String imageName = UUID.randomUUID().toString().replace("-", "");
            String replaceFileName = null;
            try {
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, file);
            } catch (IOException e) {
                FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
                throw new DataUpdateException("이미지 업로드 실패");
            }
            ProjectPostFileDTO projectPostFileDTO = new ProjectPostFileDTO();
            projectPostFileDTO.setProjectPostFileOgFile(imageName);
            projectPostFileDTO.setProjectPostFileChangedFile(replaceFileName);
            projectPostFileDTO.setProjectPostFileCreationDate(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
            return projectPostFileDTO;
        } catch (Exception e) {
            throw new DataUpdateException("이미지 업로드 실패");
        }
    }

    @Transactional
    public ProjectPostDTO createProjectPost(Long projectCode, ProjectPostDTO projectPostDTO, Long userEmployeeCode) {
        try {
            ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("현재 계정은 해당 프로젝트의 멤버가 아닙니다."));
            ProjectPost projectPost = modelMapper.map(projectPostDTO, ProjectPost.class);
            projectPost.setProjectMember(projectMember);
            projectPostRepository.save(projectPost);
            ProjectPostDTO newProjectPostDTO = modelMapper.map(projectPost, ProjectPostDTO.class);
            return newProjectPostDTO;
        } catch (Exception e) {
            throw new DataUpdateException("프로젝트 게시글 생성 실패");
        }
    }

    @Transactional
    public String delegateAdminInProject(Long projectCode, Long delegateEmployeeCode, Long userEmployeeCode) {
        try {
            Project project = projectRepository.findById(projectCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 프로젝트가 존재하지 않습니다."));
            if (!project.getProjectManager().getEmployeeCode().equals(userEmployeeCode)) {
                throw new ProjectManagerException("현재 계정이 관리자가 아닙니다.");
            }
            ProjectMember projectMember = projectMemberRepository.findByProjectCodeAndProjectMemberDeleteStatusAndEmployee_EmployeeCode(projectCode, "N", delegateEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("프로젝트 멤버에 현재 위임할 계정이 존재하지 않습니다."));
            if (projectMember.getEmployee().getEmployeeCode().equals(userEmployeeCode)) {
                throw new ProjectKickedException("자기자신을 위임할 수 없습니다.");
            }
            project = project.setProjectManager(projectMember.getEmployee())
                    .builder();
            projectRepository.save(project);
            return "프로젝트 멤버 위임하기 성공";
        } catch (Exception e) {
            throw new DataDeletionException("프로젝트 멤버 위임하기 실패");
        }
    }
}
