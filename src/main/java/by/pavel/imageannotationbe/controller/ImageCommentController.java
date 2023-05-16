package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.ImageCommentDto;
import by.pavel.imageannotationbe.model.ImageComment;
import by.pavel.imageannotationbe.service.ImageCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/images/{imageId}/comments")
public class ImageCommentController {

    private final ImageCommentService commentService;

    @GetMapping
    public List<ImageCommentDto> getComments(@PathVariable Long projectId, @PathVariable UUID imageId) {
        return commentService.getAll(projectId, imageId);
    }

    @PostMapping
    public ImageCommentDto addComment(
            @PathVariable Long projectId,
            @PathVariable UUID imageId,
            @RequestBody ImageCommentDto commentDto) {
        return commentService.addComment(projectId, imageId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void removeComment(@PathVariable Long projectId, @PathVariable UUID imageId, @PathVariable Long commentId) {
        commentService.removeComment(projectId, imageId, commentId);
    }

    @PutMapping("/{commentId}/resolve")
    public void setResolved(
            @PathVariable Long projectId,
            @PathVariable UUID imageId,
            @PathVariable Long commentId,
            @RequestParam boolean isResolved) {
        commentService.setResolved(projectId, imageId, commentId, isResolved);

    }

}
