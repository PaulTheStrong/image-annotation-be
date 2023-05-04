package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.BoundingBoxAnnotationDto;
import by.pavel.imageannotationbe.service.BoundingBoxAnnotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/annotations/bbox/{imageId}")
@RequiredArgsConstructor
public class BoundingBoxAnnotationController {

    private final BoundingBoxAnnotationService annotationService;

    @GetMapping
    public List<BoundingBoxAnnotationDto> getAllBoundingBoxesAnnotations(@PathVariable Long imageId) {
        return annotationService.getBoundingBoxesAnnotations(imageId);
    }

    @PostMapping
    public BoundingBoxAnnotationDto addBoundingBoxAnnotation(@PathVariable Long imageId, @RequestBody BoundingBoxAnnotationDto dto) {
        return annotationService.addBoundingBoxAnnotationInDb(imageId, dto);
    }

    @PutMapping("/{annotationId}")
    public BoundingBoxAnnotationDto updateBoundingBoxAnnotation(
            @PathVariable Long imageId,
            @PathVariable Long annotationId,
            @RequestBody BoundingBoxAnnotationDto dto
    ) {
        return annotationService.updateBoundingBoxAnnotationInDb(imageId, annotationId, dto);
    }

}
