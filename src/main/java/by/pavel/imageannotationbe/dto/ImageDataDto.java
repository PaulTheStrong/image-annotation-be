package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.model.ImageComment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public record ImageDataDto (
        Long id,
        Long projectId,
        String fileName,
        Integer width,
        Integer height,
        List<ImageCommentDto> comments) {

    public static ImageDataDto toDto(AnnotationImage img) {
        return new ImageDataDto(
                img.getId(),
                img.getProject().getId(),
                img.getImageName(),
                img.getWidth(),
                img.getHeight(),
                img.getComments().stream().map(ImageCommentDto::ofEntity).toList()
        );
    }

    public static ImageDataDto toDtoNoComments(AnnotationImage image) {
        return new ImageDataDto(
                image.getId(),
                image.getProject().getId(),
                image.getImageName(),
                image.getWidth(),
                image.getHeight(),
                Collections.emptyList()
        );
    }
}
