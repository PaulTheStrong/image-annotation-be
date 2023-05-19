package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.ProjectRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRoleRepository extends CrudRepository<ProjectRole, Long> {

    List<ProjectRole> findAllByUserIdAndProjectId(Long userId, Long projectId);

    List<ProjectRole> findAllByUserId(Long userId);

    boolean existsByUserIdAndProjectId(Long userId, Long projectId);

}
