package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.AnnotationTag;
import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.repository.AnnotationTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnotationTagService {

    private final AnnotationTagRepository tagRepository;

    @Transactional
    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    public AnnotationTag addTag(Long projectId, AnnotationTag annotationTag) {
        annotationTag.setId(null);
        annotationTag.setProject(Project.builder().id(projectId).build());
        tagRepository.save(annotationTag);
        return annotationTag;
    }

    @PreAuthorize("@projectSecurityService.canReadProject(#projectId)")
    public List<AnnotationTag> getProjectTags(Long projectId) {
        return tagRepository.findAllByProjectIdOrderById(projectId);
    }

    @Transactional
    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    public void removeTag(Long projectId, Long annotationTagId) {
        tagRepository.deleteByProjectIdAndId(projectId, annotationTagId);
    }

    @Transactional
    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    public AnnotationTag patchTag(Long projectId, Long tagId, AnnotationTag annotationTag) {
        AnnotationTag existingTag = tagRepository.findByIdAndProjectId(tagId, projectId)
                .orElseThrow(() -> new NotFoundException("Annotation tag not found"));
        existingTag.setName(annotationTag.getName());
        existingTag.setColor(annotationTag.getColor());
        tagRepository.save(existingTag);
        return existingTag;
    }
}
