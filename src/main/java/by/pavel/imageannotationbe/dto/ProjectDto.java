package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.AnnotationType;
import by.pavel.imageannotationbe.model.Project;

public record ProjectDto(Long id, String name, String description, AnnotationType allowedAnnotationType) {

    public static ProjectDto toDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getAllowedAnnotationType()
        );
    }

}
