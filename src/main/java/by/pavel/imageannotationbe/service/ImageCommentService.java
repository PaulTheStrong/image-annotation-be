package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.ImageCommentDto;
import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.model.ImageComment;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.repository.AnnotationImageRepository;
import by.pavel.imageannotationbe.repository.ImageCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageCommentService {

    private final AnnotationImageRepository imageRepository;
    private final ImageCommentRepository commentRepository;

    public List<ImageCommentDto> getAll(Long projectId, UUID imageId) {
        return imageRepository.getByProjectIdAndId(projectId, imageId)
                .map(AnnotationImage::getComments)
                .orElseThrow(() -> new NotFoundException("Image " + imageId + " not found"))
                .stream().map(ImageCommentDto::ofEntity).toList();
    }

    public ImageCommentDto addComment(Long projectId, UUID imageId, ImageCommentDto dto) {
        AnnotationImage annotationImage = imageRepository.getByProjectIdAndId(projectId, imageId)
                .orElseThrow(() -> new NotFoundException("Image " + imageId + " not found"));
        ImageComment imageComment = ImageComment.builder()
                .image(annotationImage)
                .text(dto.text())
                .owner(User.builder().id(1L).build())
                .createdAt(LocalDateTime.now())
                .build();
        imageComment = commentRepository.save(imageComment);
        return ImageCommentDto.ofEntity(commentRepository.findById(imageComment.getId()).get());
    }

    @Transactional
    public void removeComment(Long projectId, UUID imageId, Long commentId) {
        AnnotationImage annotationImage = imageRepository.getByProjectIdAndId(projectId, imageId)
                .orElseThrow(() -> new NotFoundException("Image " + imageId + " not found"));

        boolean isRemoved = annotationImage.getComments().removeIf(comment -> comment.getId().equals(commentId));
        if (!isRemoved) {
            throw new NotFoundException("Comment " + commentId + " not found");
        }
        commentRepository.deleteById(commentId);
        imageRepository.save(annotationImage);
    }

    @Transactional
    public void setResolved(Long projectId, UUID imageId, Long commentId, boolean isResolved) {
        ImageComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment " + commentId + " not found"));

        comment.setResolved(isResolved);
    }
}
