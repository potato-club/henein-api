package com.example.demo.repository;

import com.example.demo.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    @Query("SELECT c FROM CommentEntity c JOIN FETCH c.boardEntity WHERE c.boardEntity.id =:id")
    public List<CommentEntity> findByBoardEntity(@Param("id")Long id);
}
