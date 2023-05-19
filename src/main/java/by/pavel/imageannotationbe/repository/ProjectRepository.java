package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findAllByOwnerId(Long userId);

    boolean existsByOwnerIdAndId(Long userId, Long projectId);

    @Query(nativeQuery = true, value = """
        SELECT * FROM project WHERE id IN (
            SELECT DISTINCT project_role_project_id FROM project_role WHERE project_role_user_id = ?1
        )
    """)
    List<Project> findAccessedProjects(Long userId);
}
