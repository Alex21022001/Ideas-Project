package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.exception.UploadingFileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    @Value("${aws.s3.projects-url}")
    private final String PROJECT_BASE_URL;
    @Value("${aws.s3.bucket.name}")
    private final String BUCKET;

    private final S3Client s3Client;

    public Optional<String> upload(MultipartFile file, Class<?> clazz) {
        String uniqueName = generateUniqueName(file.getOriginalFilename());
        String path = buildPath(uniqueName, clazz);

        upload(file, path);
        return Optional.of(uniqueName);
    }

    private void upload(MultipartFile file, String path) throws UploadingFileException {
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .key(path)
                    .bucket(BUCKET)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, file.getSize()));
            log.debug("File:{} was uploaded successfully to path:{}", file.getName(), path);
        } catch (S3Exception s3Exception) {
            log.error("Didn't manage to upload file:{} to path:{}. Exception:{}", file.getName(), path, s3Exception);
            throw new UploadingFileException("Couldn't load file: " + file.getOriginalFilename(),
                    file.getOriginalFilename(), file.getContentType());
        } catch (IOException e) {
            log.error("Didn't manage to read file:{}. Exception:{}", file.getName(), e);
            throw new UploadingFileException("Couldn't load file: " + file.getOriginalFilename(),
                    file.getOriginalFilename(), file.getContentType());
        }
    }

    private String buildPath(String filename, Class<?> clazz) {
        if (clazz.equals(Project.class)) {
            return PROJECT_BASE_URL + filename;
        }
        throw new IllegalArgumentException("There is no such a path for " + clazz.getName());
    }

    private String generateUniqueName(String filename) {
        String extension = filename.substring(filename.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }
}

