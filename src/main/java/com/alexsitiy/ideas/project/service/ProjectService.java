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

    private final S3Service s3Service;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final ProjectCreateMapper projectCreateMapper;
    private final ProjectReadMapper projectReadMapper;

    @Transactional
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

                    uploadFile(projectDto.getImage())
                            .ifPresent(project::setImagePath);

                    uploadFile(projectDto.getDocs())
                            .ifPresent(project::setDocsPath);

                    user.addProject(project);
                    projectRepository.save(project);
                    log.debug("Project {} was created", project);
                    return project;
                })
                .map(projectReadMapper::map);
    }

    private Optional<String> uploadFile(MultipartFile file) {
        if (file.isEmpty())
            return Optional.empty();
        else {
            return s3Service.upload(file, Project.class);
        }
    }
}
