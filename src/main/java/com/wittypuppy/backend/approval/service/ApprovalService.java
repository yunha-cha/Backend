package com.wittypuppy.backend.approval.service;

import com.wittypuppy.backend.approval.dto.ApprovalDocDTO;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import com.wittypuppy.backend.approval.repository.ApprovalDocRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ApprovalService {
    private final ModelMapper modelMapper;
    private final ApprovalDocRepository approvalDocRepository;


    public ApprovalService(ModelMapper modelMapper, ApprovalDocRepository approvalDocRepository) {
        this.modelMapper = modelMapper;
        this.approvalDocRepository = approvalDocRepository;
    }

    public ApprovalDocDTO selectInboxDoc(Long approvalDocCode) {
        log.info("[ApprovalService] selectInboxDoc start=====");

        ApprovalDoc approvalDoc = approvalDocRepository.findById(approvalDocCode).get();
        ApprovalDocDTO approvalDocDTO = modelMapper.map(approvalDoc, ApprovalDocDTO.class);

        log.info("[ApprovalService] selectInboxDoc End=====");

        return approvalDocDTO;
    }

    @Transactional
    public String submitApproval(ApprovalDocDTO approvalDocDTO) {
        log.info("[ApprovalService] submitApproval start=====");

        int result = 0;

        ApprovalDoc newDoc = modelMapper.map(approvalDocDTO, ApprovalDoc.class);
        newDoc.setApprovalRequestDate(LocalDateTime.now());
        approvalDocRepository.save(newDoc);
        result = 1;

    return result > 0 ? "상신 성공" : "상신 실패";
    }
}
