package com.example.demo.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.access-key}") // application.yml 에 명시한 내용
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;
    @Bean
    public AmazonS3 amazonS3(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
//    AWSCredentials 객체는 AWS 액세스 키와 비밀 액세스 키를 캡슐화하는 인터페이스입니다.
//    BasicAWSCredentials는 AWSCredentials 인터페이스의 구현 클래스 중 하나이며, AWS 액세스 키와 비밀 액세스 키를 사용하여 객체를 생성할 수 있습니다.
//    AmazonS3ClientBuilder 클래스는 AmazonS3 인터페이스를 생성하기 위한 빌더 클래스입니다.
//    AmazonS3ClientBuilder를 사용하여 AWS S3 클라이언트를 생성하면 다양한 구성 옵션을 설정할 수 있습니다.
//    위의 코드에서는 withRegion() 메서드를 사용하여 S3 버킷이 위치한 지역을 설정하고,
//    withCredentials() 메서드를 사용하여 AWS 액세스 키와 비밀 액세스 키를 포함하는 AWSCredentials 객체를 전달합니다.
//    build() 메서드를 호출하여 S3 클라이언트를 생성합니다.
//
//    따라서, 위의 코드를 통해 AWS S3 클라이언트를 생성하고,
//    AWS 액세스 키와 비밀 액세스 키, S3 버킷의 위치 정보를 사용하여 AWS S3와 연결할 수 있습니다.
}
