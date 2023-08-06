package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record ImageDataDto (
        String id,
        Long projectId,
        String fileName,
        Integer width,
        Integer height,
        List<ImageCommentDto> comments,
        Integer status,
        String annotatedBy) {

    public static ImageDataDto toDto(AnnotationImage img) {
        return new ImageDataDto(
                img.getId().toString(),
                img.getProject().getId(),
                img.getImageName(),
                img.getWidth(),
                img.getHeight(),
                img.getComments().stream().map(ImageCommentDto::ofEntity).toList(),
                img.getStatus().ordinal(),
                Optional.ofNullable(img.getAnnotatedBy()).map(User::getEmail).orElse(null)
        );
    }

    public static ImageDataDto toDtoNoComments(AnnotationImage img) {
        return new ImageDataDto(
                img.getId().toString(),
                img.getProject().getId(),
                img.getImageName(),
                img.getWidth(),
                img.getHeight(),
                Collections.emptyList(),
                img.getStatus().ordinal(),
                Optional.ofNullable(img.getAnnotatedBy()).map(User::getEmail).orElse(null)
        );
    }

}
