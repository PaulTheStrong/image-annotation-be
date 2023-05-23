package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.InvitationBatchDto;
import by.pavel.imageannotationbe.dto.InvitationCreateDto;
import by.pavel.imageannotationbe.dto.ProjectInvitationDto;
import by.pavel.imageannotationbe.service.ProjectInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/invitations")
@RequiredArgsConstructor
public class ProjectInvitationController {

    private final ProjectInvitationService projectInvitationService;

    @GetMapping
    public List<ProjectInvitationDto> getAllProjectInvitations(@PathVariable Long projectId) {
        return projectInvitationService.getAllProjectInvitations(projectId);
    }

    @PostMapping("/batch")
    public List<ProjectInvitationDto> createInvitations(@RequestBody InvitationBatchDto batchDto, @PathVariable Long projectId) {
        return projectInvitationService.createBatch(new InvitationBatchDto(projectId, batchDto.emailToRoleIds()));
    }

    @PostMapping
    public ProjectInvitationDto createInvitation(@RequestBody InvitationCreateDto createDto, @PathVariable Long projectId) {
        return projectInvitationService.createInvitation(projectId, createDto.email(), createDto.roleIds());
    }

    @PatchMapping("/{invitationId}")
    public ProjectInvitationDto patchInvitation(@RequestBody InvitationCreateDto patchDto, @PathVariable Long projectId, @PathVariable Long invitationId) {
        return projectInvitationService.patchInvitation(projectId, invitationId, patchDto.roleIds());
    }

    @DeleteMapping("/{invitationId}")
    public void deleteInvitation(@PathVariable Long projectId, @PathVariable Long invitationId) {
        projectInvitationService.deleteInvitation(projectId, invitationId);
    }

    @PutMapping("/accept")
    public void acceptInvitation(@PathVariable Long projectId) {
        projectInvitationService.acceptInvite(projectId);
    }

    @PutMapping("/deny")
    public void denyInvitation(@PathVariable Long projectId) {
        projectInvitationService.denyInvite(projectId);
    }
}
