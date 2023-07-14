package com.alexsitiy.ideas.project.integration;


import com.alexsitiy.ideas.project.service.S3Service;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@TestConfiguration
public class TestSpringSecurityApplication {

    @SpyBean
    private S3Service s3Service;
}
