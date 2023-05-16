package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.BoundingBoxAnnotationDto;
import by.pavel.imageannotationbe.dto.PolygonAnnotationDto;
import by.pavel.imageannotationbe.model.*;
import by.pavel.imageannotationbe.model.data.BoundingBox;
import by.pavel.imageannotationbe.model.data.Point2D;
import by.pavel.imageannotationbe.model.data.Polygon;
import by.pavel.imageannotationbe.repository.AnnotationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PolygonAnnotationService extends AbstractAnnotationService<Polygon, PolygonAnnotationDto> {

    public PolygonAnnotationService(AnnotationRepository annotationRepository, ObjectMapper objectMapper) {
        super(annotationRepository, objectMapper);
    }

    @Override
    protected AnnotationType getAnnotationType() {
        return AnnotationType.POLYGON;
    }

    @Override
    @SneakyThrows
    protected PolygonAnnotationDto toDto(Annotation annotation) {
        return new PolygonAnnotationDto(
                annotation.getId(),
                annotation.getAnnotationTag().getId(),
                Arrays.stream(objectMapper.readValue(annotation.getValue(), new TypeReference<Integer[][]>() {})).map(Point2D::new).toList()
        );
    }

    @Override
    @SneakyThrows
    protected Annotation toAnnotation(PolygonAnnotationDto dto, UUID imageId) {
        return new Annotation(
                dto.annotationId(),
                AnnotationImage.builder().id(imageId).build(),
                getAnnotationType(),
                null,
                StorageType.LOCAL_STORAGE,
                objectMapper.writeValueAsString(dto.polygon().stream().map(p -> new Integer[] {p.x(), p.y()}).toArray(Integer[][]::new)),
                AnnotationStatus.IN_PROGRESS,
                AnnotationTag.builder().id(dto.annotationTagId()).build()

        );
    }
}
