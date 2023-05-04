package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.AnnotationType;

public record ProjectDto(Long id, String name, String description, AnnotationType allowedAnnotationType) {

}
