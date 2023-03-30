package com.example.demo.repository;

import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.RecommendEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommandRepository extends JpaRepository<RecommendEntity,Long> {
    RecommendEntity findByBoardEntityAndUserEntity(BoardEntity boardEntity, UserEntity userEntity);
}
