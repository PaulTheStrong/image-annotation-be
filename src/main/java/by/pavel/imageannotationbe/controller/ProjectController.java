package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.CreateProjectDto;
import by.pavel.imageannotationbe.dto.ProjectDto;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.service.ProjectService;
import by.pavel.imageannotationbe.service.security.ProjectSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectDto> getOwnedProjects() {
        return projectService.getOwnedProjects();
    }

    @GetMapping("/accessed")
    public List<ProjectDto> getAccessedProjects() {
        return projectService.getAccessedProjects();
    }

    @GetMapping("/{projectId}")
    public ProjectDto getById(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @PutMapping("/{projectId}")
    public ProjectDto updateProject(@PathVariable Long projectId, @RequestBody CreateProjectDto project) {
        return projectService.updateProject(projectId, project);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.removeProject(projectId);
    }

    @PostMapping
    public ProjectDto createProject(@RequestBody CreateProjectDto project) {
        return projectService.createProject(project);
    }

}
