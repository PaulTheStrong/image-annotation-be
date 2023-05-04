package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.AnnotationTag;

public record AnnotationTagDto(Long id, String name, String color) {

    public static AnnotationTagDto ofEntity(AnnotationTag entity) {
        return new AnnotationTagDto(entity.getId(), entity.getName(), entity.getColor());
    }

    public AnnotationTag toEntity() {
        return AnnotationTag.builder().id(id).name(name).color(color).build();
    }

}
