package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.ImageDataDto;
import by.pavel.imageannotationbe.service.AnnotationImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/images")
@RequiredArgsConstructor
public class AnnotationImageController {

    private final AnnotationImageService annotationImageService;

    @GetMapping
    public List<ImageDataDto> getAll(@PathVariable Long projectId) {
        return annotationImageService.getAllByProjectId(projectId);
    }

    @GetMapping("/{imageId}")
    public ImageDataDto getAllByProjectAndImageId(@PathVariable Long projectId, @PathVariable UUID imageId) {
        return annotationImageService.getByProjectIdAndImageId(projectId, imageId);
    }

    @GetMapping(value = "/{imageId}/downloadPreview", produces = {"image/jpeg", "image/png"})
    public @ResponseBody byte[] downloadImagePreview(@PathVariable Long projectId, @PathVariable UUID imageId) {
        return annotationImageService.downloadAnnotationImagePreview(projectId, imageId);
    }

    @GetMapping(value = "/{imageId}/download", produces = {"image/png", "image/jpeg"})
    public @ResponseBody byte[] downloadImage(@PathVariable Long projectId, @PathVariable UUID imageId) {
        return annotationImageService.downloadAnnotationImage(projectId, imageId);
    }

    @PostMapping
    public ImageDataDto uploadFile(@PathVariable Long projectId, @RequestParam("file") MultipartFile file) {
        return annotationImageService.uploadFile(projectId, file);
    }

    @DeleteMapping("/{imageId}")
    public void deleteFile(@PathVariable Long projectId, @PathVariable UUID imageId) {
        annotationImageService.deleteFile(projectId, imageId);
    }
}
