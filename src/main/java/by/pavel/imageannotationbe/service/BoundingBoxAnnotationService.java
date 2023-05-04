package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.BoundingBoxAnnotationDto;
import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.*;
import by.pavel.imageannotationbe.model.data.BoundingBox;
import by.pavel.imageannotationbe.repository.AnnotationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BoundingBoxAnnotationService {

    private final AnnotationRepository annotationRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @SneakyThrows
    public BoundingBoxAnnotationDto addBoundingBoxAnnotationInDb(Long imageId, BoundingBoxAnnotationDto dto) {
        Annotation bboxAnnotation = new Annotation(
                null,
                AnnotationImage.builder().id(imageId).build(),
                AnnotationType.BOUNDING_BOX,
                null,
                StorageType.LOCAL_STORAGE,
                objectMapper.writeValueAsString(dto.boundingBox().as2PointsArray()),
                AnnotationStatus.IN_PROGRESS,
                AnnotationTag.builder().id(dto.annotationTagId()).build()

        );
        return annotationToBBoxDto(annotationRepository.save(bboxAnnotation));
    }

    @Transactional
    @SneakyThrows
    public BoundingBoxAnnotationDto updateBoundingBoxAnnotationInDb(Long imageId, Long annotationId, BoundingBoxAnnotationDto dto) {
        Annotation existingAnnotation = annotationRepository.findByAnnotationImageIdAndIdAndAnnotationType(imageId, annotationId, AnnotationType.BOUNDING_BOX)
                .orElseThrow(() -> new NotFoundException("No bounding box annotation exist with id " + annotationId));
        existingAnnotation.setValue(objectMapper.writeValueAsString(dto.boundingBox().as2PointsArray()));
        return annotationToBBoxDto(annotationRepository.save(existingAnnotation));
    }

    public List<BoundingBoxAnnotationDto> getBoundingBoxesAnnotations(Long imageId) {
        return annotationRepository.findAllByAnnotationTypeAndAnnotationImageId(AnnotationType.BOUNDING_BOX, imageId)
                .stream()
                .map(this::annotationToBBoxDto)
                .collect(toList());
    }

    @SneakyThrows
    private BoundingBoxAnnotationDto annotationToBBoxDto(Annotation annotation) {
        return new BoundingBoxAnnotationDto(
                annotation.getId(),
                annotation.getAnnotationTag().getId(),
                BoundingBox.of2PointArray(objectMapper.readValue(annotation.getValue(), Integer[].class))
        );
    }
}
