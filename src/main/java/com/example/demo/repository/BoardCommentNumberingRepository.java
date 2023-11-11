package com.example.demo.repository;

import com.example.demo.entity.BoardCommentNumberingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentNumberingRepository extends JpaRepository<BoardCommentNumberingEntity, Long> {
    List<BoardCommentNumberingEntity> findAllByBoardId(long boardId);
    void deleteByBoardIdAndUserEmail(long boardId, String userEmail);
}
