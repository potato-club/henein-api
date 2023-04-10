package com.example.demo.dto.file;

import com.example.demo.entity.S3File;
import lombok.Getter;

@Getter
public class FileResponseDto {
    private String fileName;
    private String fileUrl;

    public FileResponseDto(S3File s3file) {
        this.fileName = s3file.getFileName();
        this.fileUrl = s3file.getFileUrl();
    }
}
