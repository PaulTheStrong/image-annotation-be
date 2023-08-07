package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.dto.InvitationBatchDto;
import by.pavel.imageannotationbe.dto.ProjectInvitationDto;
import by.pavel.imageannotationbe.exception.BadRequestException;
import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.ProjectInvitation;
import by.pavel.imageannotationbe.model.ProjectRole;
import by.pavel.imageannotationbe.model.Role;
import by.pavel.imageannotationbe.model.User;
import by.pavel.imageannotationbe.repository.ProjectInvitationRepository;
import by.pavel.imageannotationbe.repository.ProjectRepository;
import by.pavel.imageannotationbe.repository.ProjectRoleRepository;
import by.pavel.imageannotationbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectInvitationService implements UserAware {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectInvitationRepository projectInvitationRepository;
    private final ProjectRoleRepository projectRoleRepository;

    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    @Transactional
    public ProjectInvitationDto createInvitation(Long projectId, String userEmail, Set<Long> roleIds) {
        if (userEmail.equals(getCurrentUser().getUsername())) {
            throw new BadRequestException("Cannot invite yourself to project");
        }
        User invitedUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User with email " + userEmail + " doesn't exist"));
        if (projectInvitationRepository.findByProjectIdAndInvitedUserId(projectId, invitedUser.getId()).isPresent()) {
            throw new BadRequestException("Cannot create one more invite for " + userEmail + " user");
        }

        ProjectInvitation newInvitation = ProjectInvitation.builder()
                .project(Project.builder().id(projectId).build())
                .invitedUser(invitedUser)
                .roles(roleIds.stream().map(roleId -> Role.builder().id(roleId).build()).collect(Collectors.toSet()))
                .build();

        ProjectInvitation saved = projectInvitationRepository.save(newInvitation);
        return new ProjectInvitationDto(saved.getId(), userEmail, roleIds);
    }

    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    @Transactional
    public ProjectInvitationDto patchInvitation(Long projectId, Long invitationId, Set<Long> roleIds) {
        Optional<ProjectInvitation> invite = projectInvitationRepository.findByIdAndProjectId(invitationId, projectId);
        if (invite.isEmpty()) {
            throw new NotFoundException("Cannot find invite with id " + invitationId);
        }
        ProjectInvitation invitation = invite.get();
        invitation.setRoles(roleIds.stream()
                .map(roleId -> Role.builder().id(roleId).build())
                .collect(Collectors.toSet())
        );

        ProjectInvitation saved = projectInvitationRepository.save(invitation);
        return new ProjectInvitationDto(saved.getId(), invitation.getInvitedUser().getEmail(), roleIds);
    }

    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    @Transactional
    public void deleteInvitation(Long projectId, Long invitationId) {
        Optional<ProjectInvitation> invite = projectInvitationRepository.findByIdAndProjectId(invitationId, projectId);
        if (invite.isEmpty()) {
            throw new NotFoundException("Cannot find invite with id " + invitationId);
        }
        projectInvitationRepository.deleteById(invitationId);
    }

    @Transactional
    @PreAuthorize("@projectSecurityService.canEditProject(#invitations.projectId())")
    public List<ProjectInvitationDto> createBatch(InvitationBatchDto invitations) {
        Long projectId = invitations.projectId();
        Map<String, Set<Long>> roles = invitations.emailToRoleIds();
        return roles.entrySet().stream()
                .map((entry) -> createInvitation(projectId, entry.getKey(), entry.getValue()))
                .toList();
    }

    @Transactional
    public void acceptInvite(Long projectId) {
        Long currentUserId = getCurrentUser().getId();
        ProjectInvitation invitation = projectInvitationRepository
                .findByProjectIdAndInvitedUserId(projectId, currentUserId)
                .orElseThrow(() -> getNotFoundException(projectId, currentUserId));

        List<ProjectRole> projectRoles = invitation.getRoles().stream()
                .map(inviteRole -> ProjectRole.builder()
                        .user(User.builder().id(currentUserId).build())
                        .role(inviteRole)
                        .project(Project.builder().id(projectId).build())
                        .build()
                ).toList();

        projectRoleRepository.saveAll(projectRoles);
        projectInvitationRepository.delete(invitation);
    }

    private static NotFoundException getNotFoundException(Long projectId, Long currentUserId) {
        return new NotFoundException("Invitation not found for project " + projectId + " and user " + currentUserId);
    }

    @Transactional
    public void denyInvite(Long projectId) {
        Long currentUserId = getCurrentUser().getId();
        ProjectInvitation invitation = projectInvitationRepository
                .findByProjectIdAndInvitedUserId(projectId, currentUserId)
                .orElseThrow(() -> getNotFoundException(projectId, currentUserId));
        projectInvitationRepository.delete(invitation);
    }

    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    public List<ProjectInvitationDto> getAllProjectInvitations(Long projectId) {
        return projectInvitationRepository.findAllByProjectId(projectId)
                .stream()
                .map(prjInvite -> new ProjectInvitationDto(
                        prjInvite.getId(),
                        prjInvite.getInvitedUser().getEmail(),
                        prjInvite.getRoles().stream().map(Role::getId).collect(Collectors.toSet())))
                .toList();
    }

}
