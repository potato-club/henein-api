package kr.henein.api.repository;

import kr.henein.api.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long>{

}
