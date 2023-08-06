package by.pavel.imageannotationbe.controller;

import by.pavel.imageannotationbe.dto.AnnotationTagDto;
import by.pavel.imageannotationbe.model.AnnotationTag;
import by.pavel.imageannotationbe.service.AnnotationTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static by.pavel.imageannotationbe.dto.AnnotationTagDto.ofEntity;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/projects/{projectId}/tags")
@RequiredArgsConstructor
public class AnnotationTagController {

    private final AnnotationTagService tagService;

    @GetMapping
    public List<AnnotationTagDto> getProjectTags(@PathVariable Long projectId) {
        return tagService.getProjectTags(projectId).stream().map(AnnotationTagDto::ofEntity).collect(toList());
    }

    @PostMapping
    public AnnotationTagDto addTag(@RequestBody AnnotationTag annotationTag, @PathVariable Long projectId) {
        return ofEntity(tagService.addTag(projectId, annotationTag));
    }

    @DeleteMapping("/{tagId}")
    public void removeTag(@PathVariable Long projectId, @PathVariable Long tagId) {
        tagService.removeTag(projectId, tagId);
    }

    @PatchMapping("/{tagId}")
    public AnnotationTagDto updateAnnotationTag(
            @PathVariable Long projectId,
            @PathVariable Long tagId,
            @RequestBody AnnotationTag tag
    ) {
        return ofEntity(tagService.patchTag(projectId, tagId, tag));
    }

}
