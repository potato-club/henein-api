package com.example.demo.repository;

import com.example.demo.entity.AdvertiseBoardEntity;
import com.example.demo.entity.NoticeBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity,Long> {

}
