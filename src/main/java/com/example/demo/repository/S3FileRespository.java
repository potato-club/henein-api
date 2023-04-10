package com.example.demo.repository;

import com.example.demo.entity.S3File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FileRespository extends JpaRepository<S3File,Long> {
}
