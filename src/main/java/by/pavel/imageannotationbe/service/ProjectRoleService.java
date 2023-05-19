package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.model.ProjectRole;
import by.pavel.imageannotationbe.repository.ProjectRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRoleService {

    private final ProjectRoleRepository projectRoleRepository;

    public List<ProjectRole> getRolesByProjectIdAndUserId(Long projectId, Long userId) {
        return projectRoleRepository.findAllByUserIdAndProjectId(userId, projectId);
    }

    public List<ProjectRole> getRolesByUserId(Long userId) {
        return projectRoleRepository.findAllByUserId(userId);
    }

    public boolean anyRoleExists(Long projectId, Long userId) {
        return projectRoleRepository.existsByUserIdAndProjectId(userId, projectId);
    }
}
