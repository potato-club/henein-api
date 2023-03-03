package com.example.demo.repository;

import com.example.demo.entity.BoardEntity;
import com.example.demo.enumCustom.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    public List<BoardEntity> findByBoardType(BoardType boardType);
    @Query("SELECT b FROM BoardEntity b WHERE NOT b.boardType = com.example.demo.enumCustom.BoardType.Notice ORDER BY b.id DESC")
    public List<BoardEntity> findAllNotNotice();
    //public List<BoardEntity> findAllNotNotice(@Param("boardType") BoardType boardType);
}
