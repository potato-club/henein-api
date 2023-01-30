package com.example.demo.repository;

import com.example.demo.entity.HumorBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HumorBoardRepository extends JpaRepository<HumorBoardEntity,Long> {

}
