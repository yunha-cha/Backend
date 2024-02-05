package com.wittypuppy.backend.approval.service;

import com.wittypuppy.backend.approval.dto.ApprovalDocDTO;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import com.wittypuppy.backend.approval.repository.ApprovalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApprovalService {

    private final ModelMapper modelMapper;
    private final ApprovalRepository approvalRepository;

    public ApprovalService(ModelMapper modelMapper, ApprovalRepository approvalRepository) {
        this.modelMapper = modelMapper;
        this.approvalRepository = approvalRepository;
    }

    public ApprovalDocDTO submitApproval(ApprovalDocDTO approvalDocDTO) {

        return null;
    }
}
