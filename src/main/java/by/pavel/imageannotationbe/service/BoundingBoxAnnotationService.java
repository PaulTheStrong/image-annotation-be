package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.BoundingBoxAnnotationDto;
import by.pavel.imageannotationbe.model.*;
import by.pavel.imageannotationbe.model.data.BoundingBox;
import by.pavel.imageannotationbe.repository.AnnotationImageRepository;
import by.pavel.imageannotationbe.repository.AnnotationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BoundingBoxAnnotationService extends AbstractAnnotationService<BoundingBox, BoundingBoxAnnotationDto> {

    public BoundingBoxAnnotationService(AnnotationRepository annotationRepository, AnnotationImageRepository imageRepository, ObjectMapper objectMapper) {
        super(annotationRepository, imageRepository, objectMapper);
    }

    @Override
    protected AnnotationType getAnnotationType() {
        return AnnotationType.BOUNDING_BOX;
    }

    @Override
    @SneakyThrows
    protected BoundingBoxAnnotationDto toDto(Annotation annotation) {
        return new BoundingBoxAnnotationDto(
                annotation.getId(),
                annotation.getAnnotationTag().getId(),
                BoundingBox.of2PointArray(objectMapper.readValue(annotation.getValue(), Integer[].class))
        );
    }

    @Override
    @SneakyThrows
    protected Annotation toAnnotation(BoundingBoxAnnotationDto dto, UUID imageId) {
        return new Annotation(
                dto.annotationId(),
                AnnotationImage.builder().id(imageId).build(),
                getAnnotationType(),
                null,
                StorageType.LOCAL_STORAGE,
                objectMapper.writeValueAsString(dto.boundingBox().as2PointsArray()),
                AnnotationTag.builder().id(dto.annotationTagId()).build()
        );
    }
}
