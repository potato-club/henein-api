package kr.henein.api.repository;

import kr.henein.api.entity.BoardCommentNumberingEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardCommentNumberingRepository extends JpaRepository<BoardCommentNumberingEntity, Long> {

}
