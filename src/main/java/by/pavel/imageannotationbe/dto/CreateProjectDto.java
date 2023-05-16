package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.ProjectInvitation;

import java.util.Set;

public record CreateProjectDto(ProjectDto project, Set<AnnotationTagDto> tags, Set<ProjectInvitation> invites) {
}
