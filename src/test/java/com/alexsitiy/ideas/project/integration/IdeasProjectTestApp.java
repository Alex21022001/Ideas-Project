package com.alexsitiy.ideas.project.integration;


import com.alexsitiy.ideas.project.service.S3Service;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class IdeasProjectTestApp {

    @SpyBean
    private S3Service s3Service;

    @SpyBean
    private JavaMailSender javaMailSender;

}
