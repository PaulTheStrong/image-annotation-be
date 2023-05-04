package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.ImageDataDto;
import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.service.AnnotationImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ImageDataDto getAllByProjectAndImageId(@PathVariable Long projectId, @PathVariable Long imageId) {
        return annotationImageService.getByProjectIdAndImageId(projectId, imageId);
    }

    @GetMapping("/{imageId}/downloadPreview")
    public @ResponseBody byte[] downloadImagePreview(@PathVariable Long projectId, @PathVariable Long imageId) {
        return annotationImageService.downloadAnnotationImagePreview(projectId, imageId);
    }

    @GetMapping("/{imageId}/download")
    public @ResponseBody byte[] downloadImage(@PathVariable Long projectId, @PathVariable Long imageId) {
        return annotationImageService.downloadAnnotationImage(projectId, imageId);
    }

    @PostMapping
    public ImageDataDto uploadFile(@PathVariable Long projectId, @RequestParam("file") MultipartFile file) {
        return annotationImageService.uploadFile(projectId, file);
    }
}
