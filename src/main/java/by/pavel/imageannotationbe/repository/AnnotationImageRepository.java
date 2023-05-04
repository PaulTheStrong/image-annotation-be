package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.AnnotationImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnotationImageRepository extends CrudRepository<AnnotationImage, Long> {

    List<AnnotationImage> getAllByProjectId(Long projectId);

    Optional<AnnotationImage> getByProjectIdAndId(Long projectId, Long id);

}
