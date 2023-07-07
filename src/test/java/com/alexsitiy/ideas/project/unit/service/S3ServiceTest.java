package com.alexsitiy.ideas.project.unit.service;

import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.service.S3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    private S3Service s3Service;
    private S3Client s3Client;

    @BeforeEach
    void init() {
        s3Client = mock(S3Client.class);
        s3Service = new S3Service("/projects","some-bucket", s3Client);
    }

    @Test
    void upload() {
        MockMultipartFile file = new MockMultipartFile(
                "TestFile",
                "image.png",
                "image/png",
                new byte[123]
        );

        Optional<String> expected = s3Service.upload(file, Project.class);

        Assertions.assertThat(expected).isPresent()
                .get().asString().contains(".png");
        verify(s3Client,times(1)).putObject(any(PutObjectRequest.class),any(RequestBody.class));
    }
}