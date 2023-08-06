package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.AnnotationTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnotationTagRepository extends CrudRepository<AnnotationTag, Long> {

    List<AnnotationTag> findAllByProjectIdOrderById(Long projectId);

    void deleteByProjectIdAndId(Long projectId, Long id);

    Optional<AnnotationTag> findByIdAndProjectId(Long id, Long projectId);

}
