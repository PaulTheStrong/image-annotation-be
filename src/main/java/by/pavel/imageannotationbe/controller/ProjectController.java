package by.pavel.imageannotationbe.controller;

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
    public List<Project> getAllProjects(User user) {
        return projectService.getAllProjects(user);
    }

    @PostMapping
    public Project createProject(User user, @RequestBody ProjectDto project) {
        return projectService.createProject(project, user);
    }

}
