package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AnnotationRepository extends CrudRepository<Annotation, Long> {

    List<Annotation> findAllByAnnotationTypeAndAnnotationImageId(AnnotationType annotationType, UUID annotationImageId);

    Optional<Annotation> findByAnnotationImageIdAndIdAndAnnotationType(UUID annotationImageId, Long id, AnnotationType annotationType);

    long deleteAnnotationByAnnotationImageIdAndIdAndAnnotationType(UUID annotationImageId, Long id, AnnotationType annotationType);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {
            "annotationImage.imageName",
            "annotationTag.id"
    })
    List<Annotation> findAllByAnnotationImageIdIn(Set<UUID> imageIds);
}
