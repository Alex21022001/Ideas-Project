package com.alexsitiy.ideas.project.service;

import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.exception.UploadingFileException;
import com.alexsitiy.ideas.project.mapper.ProjectCreateMapper;
import com.alexsitiy.ideas.project.mapper.ProjectReadMapper;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final ProjectCreateMapper projectCreateMapper;
    private final ProjectReadMapper projectReadMapper;

    public Optional<ProjectReadDto> create(ProjectCreateDto projectDto, Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    // TODO: 04.07.2023
                    //  1) Map to Project
                    //  3) Check Files existing
                    //  4) Add files to S3
                    //  5) Set generated paths + Set defaults
                    //  6) Add project to user
                    //  7) Save User + Project
                    Project project = projectCreateMapper.map(projectDto);

                    Optional<String> imagePath = uploadFile(projectDto.getImage());
                    imagePath.ifPresentOrElse(project::setImagePath, () -> {
                        log.warn("Couldn't upload file {} to S3", projectDto.getImage().getOriginalFilename());
                        throw new UploadingFileException("Couldn't load file: " + projectDto.getImage().getOriginalFilename());
                    });

                    Optional<String> docsPath = uploadFile(projectDto.getDocs());
                    docsPath.ifPresent(project::setDocsPath);

                    user.addProject(project);
                    userRepository.saveAndFlush(user);
                    log.debug("Project {} was created",project);
                    return project;
                })
                .map(projectReadMapper::map);
    }

    private Optional<String> uploadFile(MultipartFile file) {
        // TODO: 04.07.2023 S3 upload method
        // 2) check existence
        // 3) add to S3
        // 4) set generated paths
        return Optional.empty();
    }
}
