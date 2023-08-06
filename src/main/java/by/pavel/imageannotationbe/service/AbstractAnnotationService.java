package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.model.AnnotationType;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.model.data.AnnotationData;
import by.pavel.imageannotationbe.repository.AnnotationImageRepository;
import by.pavel.imageannotationbe.repository.AnnotationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public abstract class AbstractAnnotationService<T extends AnnotationData, D> implements UserAware {
    
    protected final AnnotationRepository annotationRepository;
    protected final AnnotationImageRepository imageRepository;
    protected final ObjectMapper objectMapper;
    
    protected abstract AnnotationType getAnnotationType();
    protected abstract D toDto(Annotation annotation);
    protected abstract Annotation toAnnotation(D dto, UUID imageId);


    public List<D> getAllAnnotations(UUID imageId) {
        return annotationRepository.findAllByAnnotationTypeAndAnnotationImageId(getAnnotationType(), imageId)
                .stream()
                .map(this::toDto)
                .collect(toList());
    }

    @Transactional
    @SneakyThrows
    public D addAnnotation(UUID imageId, D dto) {
        Annotation annotation = toAnnotation(dto, imageId);
        AnnotationImage annotationImage = imageRepository.findById(imageId).get();
        annotationImage.setAnnotatedBy(User.builder().id(getCurrentUser().getId()).build());
        imageRepository.save(annotationImage);
        return toDto(annotationRepository.save(annotation));
    }

    @Transactional
    public void removeAnnotation(UUID imageId, Long annotationId) {
        long deletedCount = annotationRepository.deleteAnnotationByAnnotationImageIdAndIdAndAnnotationType(imageId, annotationId, getAnnotationType());
        if (deletedCount == 0) {
            throw new NotFoundException("Annotation " + annotationId + " not found");
        } else if (deletedCount != 1) {
            throw new RuntimeException("Deleted more that one annotation by image id " + imageId + " and id " + annotationId);
        }
    }

    @Transactional
    @SneakyThrows
    public D updateAnnotation(UUID imageId, Long annotationId, D dto) {
        Annotation existingAnnotation = annotationRepository.findByAnnotationImageIdAndIdAndAnnotationType(imageId, annotationId, getAnnotationType())
                .orElseThrow(() -> new NotFoundException("No bounding box annotation exist with id " + annotationId));
        existingAnnotation.setValue(toAnnotation(dto, imageId).getValue());
        AnnotationImage annotationImage = imageRepository.findById(imageId).get();
        annotationImage.setAnnotatedBy(User.builder().id(getCurrentUser().getId()).build());
        imageRepository.save(annotationImage);
        return toDto(annotationRepository.save(existingAnnotation));
    }
}
