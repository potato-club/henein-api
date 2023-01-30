package com.example.demo.repository;

import com.example.demo.entity.FreeBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardRepository extends JpaRepository<FreeBoardEntity,Long> {

}
