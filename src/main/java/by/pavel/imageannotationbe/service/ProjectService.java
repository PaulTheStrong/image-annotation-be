package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.ProjectDto;
import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.AnnotationTag;
import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project createProject(ProjectDto projectDto, User owner) {
        Project createdProject = Project.builder()
                .name(projectDto.name())
                .description(projectDto.description())
                .owner(owner)
                .allowedAnnotationType(projectDto.allowedAnnotationType())
                .build();

        return projectRepository.save(createdProject);
    }


    public List<Project> getAllProjects(User user) {
        return projectRepository.findAllByOwner(user);
    }

}
