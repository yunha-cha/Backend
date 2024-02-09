package com.wittypuppy.backend.approval.repository;

import com.wittypuppy.backend.approval.dto.AdditionalApprovalLineDTO;
import com.wittypuppy.backend.approval.entity.AdditionalApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
public interface AdditionalApprovalLineRepository extends JpaRepository<AdditionalApprovalLine, Long> {

}
