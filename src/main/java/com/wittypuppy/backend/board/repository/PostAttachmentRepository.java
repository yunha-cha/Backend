package com.wittypuppy.backend.board.repository;

import com.wittypuppy.backend.board.entity.BoardGroup;
import com.wittypuppy.backend.board.entity.PostAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostAttachmentRepository extends JpaRepository<PostAttachment,Long> {




    List<PostAttachment> findAllByPostCode(Long postCode);
}
