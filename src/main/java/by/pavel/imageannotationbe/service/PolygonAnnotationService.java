package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.PolygonAnnotationDto;
import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.model.AnnotationTag;
import by.pavel.imageannotationbe.model.AnnotationType;
import by.pavel.imageannotationbe.model.StorageType;
import by.pavel.imageannotationbe.model.data.Point2D;
import by.pavel.imageannotationbe.model.data.Polygon;
import by.pavel.imageannotationbe.repository.AnnotationImageRepository;
import by.pavel.imageannotationbe.repository.AnnotationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class PolygonAnnotationService extends AbstractAnnotationService<Polygon, PolygonAnnotationDto> {

    public PolygonAnnotationService(
            AnnotationRepository annotationRepository,
            AnnotationImageRepository imageRepository,
            ObjectMapper objectMapper) {
        super(annotationRepository, imageRepository, objectMapper);
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
                Arrays.stream(objectMapper.readValue(annotation.getValue(), new TypeReference<Integer[][]>() { }))
                        .map(Point2D::new).toList()
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
                objectMapper.writeValueAsString(
                        dto.polygon().stream()
                        .map(p -> new Integer[] {p.x(), p.y()})
                        .toArray(Integer[][]::new)
                ),
                AnnotationTag.builder().id(dto.annotationTagId()).build()

        );
    }
}
