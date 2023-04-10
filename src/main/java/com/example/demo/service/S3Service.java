package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    /**
     * AWS S3에 이미지 파일 업로드
     * @param multipartFile : 파일
     * @return : Url
     */
    public String upload(MultipartFile multipartFile){
        String fileName = createFileName(multipartFile.getOriginalFilename());

        // s3에 이미지 저장
        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream,null));
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
        String url =amazonS3.getUrl(bucket,fileName).toString();
        return url;
    }

    // 파일 이름이 같으면 저장이 안 된다. 따라서 파일이름 앞에 UUID를 붙인다.
    private String createFileName(String fileName){
        return UUID.randomUUID()+ "-" + fileName;
    }
}
