package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.ImageComment;

import java.time.LocalDateTime;

public record ImageCommentDto(Long id, String authorEmail, String text, LocalDateTime createdAt, boolean isResolved) {

    public static ImageCommentDto ofEntity(ImageComment entity) {
        return new ImageCommentDto(entity.getId(), entity.getOwner().getEmail(), entity.getText(), entity.getCreatedAt(), entity.isResolved());
    }

    public ImageComment toEntity(ImageCommentDto dto) {
        return ImageComment.builder().id(dto.id).text(dto.text()).build();
    }
}
