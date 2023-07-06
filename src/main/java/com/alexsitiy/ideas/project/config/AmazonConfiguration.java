package com.alexsitiy.ideas.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AmazonConfiguration {

    @Value("${aws.access.key.id}")
    private final String accessKey;
    @Value("${aws.secret.access.key}")
    private final String secretKey;
    @Value("${aws.s3.region}")
    private final String region;

    @Bean
    public S3Client s3Client(){
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        return S3Client.builder()
                .credentialsProvider(provider)
                .region(Region.of(region))
                .build();
    }
}
