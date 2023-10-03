package com.example.demo.repository;

import com.example.demo.entity.S3File;
import com.example.demo.enumCustom.S3EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface S3FileRespository extends JpaRepository<S3File,Long> {
    S3File findByFileUrl(String fileName);
    List<S3File> findByS3EntityType(S3EntityType s3EntityType);
    List<S3File> findAllByS3EntityTypeAndTypeId(S3EntityType s3EntityType, Long typeId);
}
