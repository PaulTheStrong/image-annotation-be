package by.pavel.imageannotationbe.service.security;

import by.pavel.imageannotationbe.repository.ProjectRepository;
import by.pavel.imageannotationbe.security.UserDetailsImpl;
import by.pavel.imageannotationbe.service.ProjectRoleService;
import by.pavel.imageannotationbe.service.UserAware;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectSecurityService implements UserAware {

    private final ProjectRepository projectRepository;
    private final ProjectRoleService roleService;

    public boolean canReadProject(Long projectId) {
        UserDetailsImpl currentUser = getCurrentUser();
        return currentUser.isAdmin() || currentUser.isLicensed() && (
                projectRepository.existsByOwnerIdAndId(currentUser.getId(), projectId)
                || roleService.anyRoleExists(projectId, currentUser.getId()));
    }

    public boolean canEditProject(Long projectId) {
        UserDetailsImpl currentUser = getCurrentUser();
        return currentUser.isAdmin() || currentUser.isLicensed() && projectRepository.existsByOwnerIdAndId(currentUser.getId(), projectId);
    }

    public boolean canAcceptInvite(Long projectId) {
        UserDetailsImpl currentUser = getCurrentUser();
        return currentUser.isAdmin();
    }

}
