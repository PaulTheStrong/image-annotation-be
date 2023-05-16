package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.BoundingBoxAnnotationDto;
import by.pavel.imageannotationbe.service.BoundingBoxAnnotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/annotations/bbox/{imageId}")
@RequiredArgsConstructor
public class BoundingBoxAnnotationController {

    private final BoundingBoxAnnotationService annotationService;

    @GetMapping
    public List<BoundingBoxAnnotationDto> getAllAnnotations(@PathVariable UUID imageId) {
        return annotationService.getAllAnnotations(imageId);
    }

    @PostMapping
    public BoundingBoxAnnotationDto addAnnotation(@PathVariable UUID imageId, @RequestBody BoundingBoxAnnotationDto dto) {
        return annotationService.addAnnotation(imageId, dto);
    }

    @DeleteMapping("/{annotationId}")
    public void deleteAnnotation(
            @PathVariable UUID imageId,
            @PathVariable Long annotationId
    ) {
        annotationService.removeAnnotation(imageId, annotationId);
    }

    @PutMapping("/{annotationId}")
    public BoundingBoxAnnotationDto updateAnnotation(
            @PathVariable UUID imageId,
            @PathVariable Long annotationId,
            @RequestBody BoundingBoxAnnotationDto dto
    ) {
        return annotationService.updateAnnotation(imageId, annotationId, dto);
    }

}
