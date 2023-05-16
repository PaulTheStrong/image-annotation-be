package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.PolygonAnnotationDto;
import by.pavel.imageannotationbe.dto.PolygonAnnotationDto;
import by.pavel.imageannotationbe.service.BoundingBoxAnnotationService;
import by.pavel.imageannotationbe.service.PolygonAnnotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/annotations/polygon/{imageId}")
@RequiredArgsConstructor
public class PolygonAnnotationController {

    private final PolygonAnnotationService annotationService;

    @GetMapping
    public List<PolygonAnnotationDto> getAllAnnotations(@PathVariable UUID imageId) {
        return annotationService.getAllAnnotations(imageId);
    }

    @PostMapping
    public PolygonAnnotationDto addAnnotation(@PathVariable UUID imageId, @RequestBody PolygonAnnotationDto dto) {
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
    public PolygonAnnotationDto updateAnnotation(
            @PathVariable UUID imageId,
            @PathVariable Long annotationId,
            @RequestBody PolygonAnnotationDto dto
    ) {
        return annotationService.updateAnnotation(imageId, annotationId, dto);
    }

}
