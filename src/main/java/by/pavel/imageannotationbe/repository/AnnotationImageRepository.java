package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.AnnotationImage;
import org.hibernate.annotations.Fetch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnotationImageRepository extends CrudRepository<AnnotationImage, UUID> {

    List<AnnotationImage> getAllByProjectId(Long projectId);

    Optional<AnnotationImage> getByProjectIdAndId(Long projectId, UUID id);

    void deleteAllByProjectId(Long projectId);

}
