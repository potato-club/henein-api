package com.example.demo.repository;

import com.example.demo.entity.BossBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossBoardRepository extends JpaRepository<BossBoardEntity,Long> {

}
