package kr.henein.api.repository;

import kr.henein.api.entity.BoardEntity;
import kr.henein.api.enumCustom.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findByBoardTypeOrderByIdDesc(BoardType boardType, Pageable pageable);
    @Query("SELECT b FROM BoardEntity b WHERE NOT b.boardType = kr.henein.api.enumCustom.BoardType.Notice ORDER BY b.id DESC")
    Page<BoardEntity> findAllNotNotice(Pageable pageable);
    //public List<BoardEntity> findAllNotNotice(@Param("boardType") BoardType boardType);
}
