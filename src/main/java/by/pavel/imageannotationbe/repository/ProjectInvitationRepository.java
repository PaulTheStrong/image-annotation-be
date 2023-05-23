package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.ProjectInvitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectInvitationRepository extends CrudRepository<ProjectInvitation, Long> {

    Optional<ProjectInvitation> findByProjectIdAndInvitedUserId(Long projectId, Long userId);

    List<ProjectInvitation> findAllByProjectId(Long projectId);

    Optional<ProjectInvitation> findByIdAndProjectId(Long id, Long projectId);

}
