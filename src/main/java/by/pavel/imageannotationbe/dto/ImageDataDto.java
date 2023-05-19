package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.AnnotationImage;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record ImageDataDto (
        String id,
        Long projectId,
        String fileName,
        Integer width,
        Integer height,
        List<ImageCommentDto> comments) {

    public static ImageDataDto toDto(AnnotationImage img) {
        return new ImageDataDto(
                img.getId().toString(),
                img.getProject().getId(),
                img.getImageName(),
                img.getWidth(),
                img.getHeight(),
                img.getComments().stream().map(ImageCommentDto::ofEntity).toList()
        );
    }

    public static ImageDataDto toDtoNoComments(AnnotationImage img) {
        return new ImageDataDto(
                img.getId().toString(),
                img.getProject().getId(),
                img.getImageName(),
                img.getWidth(),
                img.getHeight(),
                Collections.emptyList()
        );
    }

}
