package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.CreateProjectDto;
import by.pavel.imageannotationbe.dto.ProjectDto;
import by.pavel.imageannotationbe.model.AnnotationTag;
import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.service.AnnotationTagService;
import by.pavel.imageannotationbe.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        return projectService.getAllProjects(User.builder().id(1L).build());
    }

    @GetMapping("/{projectId}")
    public ProjectDto getById(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @PutMapping("/{projectId}")
    public ProjectDto updateProject(@PathVariable Long projectId, @RequestBody CreateProjectDto project) {
        return projectService.updateProject(projectId, project, User.builder().id(1L).build());
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.removeProject(projectId);
    }

    @PostMapping
    public ProjectDto createProject(@RequestBody CreateProjectDto project) {
        return projectService.createProject(project, User.builder().id(1L).build());
    }

}
