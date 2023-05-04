package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findAllByOwner(User user);

}
