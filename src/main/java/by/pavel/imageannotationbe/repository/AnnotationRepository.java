package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnotationRepository extends CrudRepository<Annotation, Long> {

    List<Annotation> findAllByAnnotationTypeAndAnnotationImageId(AnnotationType annotationType, Long annotationImageId);

    Optional<Annotation> findByAnnotationImageIdAndIdAndAnnotationType(Long annotationImageId, Long id, AnnotationType annotationType);

}
