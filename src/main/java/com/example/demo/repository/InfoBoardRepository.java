package com.example.demo.repository;

import com.example.demo.entity.InfoBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoBoardRepository extends JpaRepository<InfoBoardEntity,Long> {

}
