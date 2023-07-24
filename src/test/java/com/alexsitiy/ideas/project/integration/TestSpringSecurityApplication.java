package com.alexsitiy.ideas.project.integration;


import com.alexsitiy.ideas.project.service.S3Service;
import jakarta.mail.internet.MimeMessage;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.PostConstruct;

@TestConfiguration
public class TestSpringSecurityApplication {

    @SpyBean
    private S3Service s3Service;

    @SpyBean
    private JavaMailSender javaMailSender;

}
