package com.alexsitiy.ideas.project.unit.service;

import com.alexsitiy.ideas.project.dto.ProjectCreateDto;
import com.alexsitiy.ideas.project.dto.ProjectReadDto;
import com.alexsitiy.ideas.project.dto.ProjectUpdateDto;
import com.alexsitiy.ideas.project.dto.UserReadDto;
import com.alexsitiy.ideas.project.entity.Project;
import com.alexsitiy.ideas.project.entity.Role;
import com.alexsitiy.ideas.project.entity.User;
import com.alexsitiy.ideas.project.integration.annotation.IT;
import com.alexsitiy.ideas.project.mapper.ProjectCreateMapper;
import com.alexsitiy.ideas.project.mapper.ProjectReadMapper;
import com.alexsitiy.ideas.project.mapper.UserReadMapper;
import com.alexsitiy.ideas.project.repository.ProjectRepository;
import com.alexsitiy.ideas.project.repository.UserRepository;
import com.alexsitiy.ideas.project.service.ProjectService;
import com.alexsitiy.ideas.project.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Service s3Service;

    @Spy
    private ProjectCreateMapper projectCreateMapper;
    @Mock
    private ProjectReadMapper projectReadMapper;


    @Test
    void create() {
        int userId = 1;
        String projectTitle = "newTitle";

        User user = User.builder()
                .id(userId)
                .username("test@gmail.com")
                .password("{bcrypt}$2a$10$4MZmbaXMS4An5Ne0Rq2Fs.9JNJZVOtAKO3yQWOpZI7dKknEvTNGYW")
                .firstname("Test1")
                .lastname("Test1")
                .role(Role.USER)
                .build();
        ProjectCreateDto projectDto = new ProjectCreateDto(
                projectTitle,
                "something",
                new MockMultipartFile("image", "project.png", "image/png", new byte[123]),
                new MockMultipartFile("doc", "project.pdf", "application/pdf", new byte[123])
        );
        ProjectReadDto projectRead = ProjectReadDto.builder()
                .id(4).build();

        doReturn(Optional.of(user)).when(userRepository).findById(userId);
        doReturn(Optional.of("filePath")).when(s3Service).upload(any(), any());
        doReturn(projectRead).when(projectReadMapper).map(any());

        Optional<ProjectReadDto> actual = projectService.create(projectDto, userId);

        assertThat(actual).isPresent()
                .map(ProjectReadDto::getId)
                .get()
                .isEqualTo(4);
        verify(projectRepository, times(1)).save(any());
    }


    @Test
    void update() {
        int projectId = 1;
        String title = "New Project";
        String description = "Something";

        ProjectUpdateDto updateDto = new ProjectUpdateDto(title, description);
        Project project = Project.builder()
                .id(projectId)
                .build();

        doReturn(Optional.of(project)).when(projectRepository).findByIdWithUser(projectId);
        doReturn(Project.builder()
                .id(projectId)
                .title(title)
                .description(description)
                .build()).when(projectRepository).saveAndFlush(project);
        doReturn(ProjectReadDto.builder()
                .id(projectId)
                .title(title)
                .description(description)
                .build()).when(projectReadMapper).map(any());

        Optional<ProjectReadDto> actual = projectService.update(projectId, updateDto);

        assertThat(actual).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("id", projectId)
                .hasFieldOrPropertyWithValue("description", description);
        verify(projectRepository,times(1)).saveAndFlush(project);
    }
}











