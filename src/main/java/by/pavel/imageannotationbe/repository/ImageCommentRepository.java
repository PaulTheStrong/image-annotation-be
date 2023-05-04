package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.ImageComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageCommentRepository extends CrudRepository<ImageComment, Long> {
}
