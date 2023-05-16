package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.config.LocalStorageConfigurationProperties;
import by.pavel.imageannotationbe.dto.CreateProjectDto;
import by.pavel.imageannotationbe.dto.ProjectDto;
import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.AnnotationTag;
import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final LocalStorageConfigurationProperties storageProps;
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectDto createProject(CreateProjectDto createProjectDto, User owner) {
        return saveProject(createProjectDto, owner);
    }

    @Transactional
    public ProjectDto updateProject(Long projectId, CreateProjectDto createProjectDto, User owner) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project " + projectId + " not found"));

        existingProject.setName(createProjectDto.project().name());
        existingProject.setDescription(createProjectDto.project().description());
        return ProjectDto.toDto(projectRepository.save(existingProject));
    }

    private ProjectDto saveProject(CreateProjectDto createProjectDto, User owner) {
        ProjectDto projectDto = createProjectDto.project();
        Set<AnnotationTag> tags = createProjectDto.tags().stream()
                .map(tag -> new AnnotationTag(null, tag.name(), tag.color(), null))
                .collect(Collectors.toSet());
        Project createdProject = Project.builder()
                .id(null)
                .name(projectDto.name())
                .description(projectDto.description())
                .owner(owner)
                .allowedAnnotationType(projectDto.allowedAnnotationType())
                .build();

        Project saved = projectRepository.save(createdProject);
        tags.forEach(tag -> tag.setProject(saved));
        saved.setTags(tags);
        return ProjectDto.toDto(projectRepository.save(saved));
    }

    public List<ProjectDto> getAllProjects(User user) {
        return projectRepository.findAllByOwner(user)
                .stream()
                .map(ProjectDto::toDto)
                .toList();
    }

    public ProjectDto getProjectById(Long projectId) {
        return ProjectDto.toDto(projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project " + projectId + " not found")));
    }

    @SneakyThrows
    @Transactional
    public void removeProject(Long projectId) {
        if (projectRepository.findById(projectId).isEmpty()) {
            throw new NotFoundException("Project " + projectId + " not found");
        }
        String imagePath = storageProps.getUploadDefaultPath().replace("{projectId}", projectId.toString());
        String previewPath = storageProps.getUploadPreviewPath().replace("{projectId}", projectId.toString());
        FileUtils.deleteDirectory(new File(imagePath));
        FileUtils.deleteDirectory(new File(previewPath));

        projectRepository.deleteById(projectId);
    }
}
