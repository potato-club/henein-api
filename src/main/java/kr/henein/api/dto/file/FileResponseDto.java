package kr.henein.api.dto.file;

import kr.henein.api.entity.S3File;
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
