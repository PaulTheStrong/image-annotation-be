package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.config.LocalStorageConfigurationProperties;
import by.pavel.imageannotationbe.dto.ImageDataDto;
import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.model.AnnotationStatus;
import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.StorageType;
import by.pavel.imageannotationbe.repository.AnnotationImageRepository;
import by.pavel.imageannotationbe.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnnotationImageService {

    public static final int COMPRESSED_IMAGE_WIDTH = 320;
    public static final int COMPRESSED_IMAGE_HEIGHT = 240;
    public static final double COMPRESSED_IMAGE_QUALITY = 0.1;

    private final AnnotationImageRepository annotationImageRepository;
    private final ProjectRepository projectRepository;
    private final LocalStorageConfigurationProperties localStorageConfigurationProperties;

    @PreAuthorize("@projectSecurityService.canReadProject(#projectId)")
    public List<ImageDataDto> getAllByProjectId(Long projectId) {
        return annotationImageRepository.getAllByProjectId(projectId)
                .stream()
                .map(ImageDataDto::toDtoNoComments)
                .toList();
    }

    public void deleteAllByProjectId(Long projectId) {
        annotationImageRepository.deleteAllByProjectId(projectId);
    }

    @PreAuthorize("@projectSecurityService.canReadProject(#projectId)")
    public ImageDataDto getByProjectIdAndImageId(Long projectId, UUID imageId) {
        return annotationImageRepository.getByProjectIdAndId(projectId, imageId)
                .map(ImageDataDto::toDto)
                .orElseThrow(() -> new NotFoundException("Annotation Image not found with " + imageId));
    }

    public byte[] downloadAnnotationImagePreview(Long projectId, UUID imageId) {
        return annotationImageRepository.getByProjectIdAndId(projectId, imageId)
                .map(annotationImage -> getImagePath(
                        localStorageConfigurationProperties.getUploadPreviewPath(),
                        projectId.toString(),
                        annotationImage.getId().toString(),
                        annotationImage.getImageName()))
                .map(AnnotationImageService::getFileBytes)
                .orElseGet(() -> new byte[0]);
    }

    public byte[] downloadAnnotationImage(Long projectId, UUID imageId) {
        return annotationImageRepository.getByProjectIdAndId(projectId, imageId)
                .map(annotationImage -> getImagePath(
                        localStorageConfigurationProperties.getUploadDefaultPath(),
                        projectId.toString(),
                        annotationImage.getId().toString(),
                        annotationImage.getImageName()))
                .map(AnnotationImageService::getFileBytes)
                .orElseGet(() -> new byte[0]);
    }

    private static byte[] getFileBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            return null;
        }
    }

    @PreAuthorize("@projectSecurityService.canReadProject(#projectId)")
    public void updateStatus(UUID imageId, Long projectId, AnnotationStatus status) {
        AnnotationImage annotationImage = annotationImageRepository.findById(imageId).get();
        annotationImage.setStatus(status);
        annotationImageRepository.save(annotationImage);
    }

    @Transactional
    @SneakyThrows
    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    public ImageDataDto uploadFile(Long projectId, MultipartFile file) {
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundException("Project with Id=" + projectId + " not found");
        }
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        AnnotationImage annotationImage = new AnnotationImage(
                UUID.randomUUID(),
                Project.builder().id(projectId).build(),
                StorageType.LOCAL_STORAGE,
                file.getOriginalFilename(),
                imageWidth,
                imageHeight,
                Collections.emptyList(),
                Collections.emptyList(),
                AnnotationStatus.IN_PROGRESS,
                null
        );
        AnnotationImage savedEntity = annotationImageRepository.save(annotationImage);
        String uploadPath = localStorageConfigurationProperties.getUploadDefaultPath()
                .replace("{projectId}", projectId.toString());
        String uploadPreviewPath = localStorageConfigurationProperties.getUploadPreviewPath()
                .replace("{projectId}", projectId.toString());
        String imageFilename = getImagePath(
                uploadPath,
                projectId.toString(),
                savedEntity.getId().toString(),
                savedEntity.getImageName());
        String previewImageFilename = getImagePath(
                localStorageConfigurationProperties.getUploadPreviewPath(),
                projectId.toString(),
                savedEntity.getId().toString(),
                savedEntity.getImageName());

        Path imagePath = Paths.get(imageFilename);

        if (!Files.exists(Paths.get(uploadPath))) {
            Files.createDirectories(Paths.get(uploadPath));
        }

        if (!Files.exists(Paths.get(uploadPreviewPath))) {
            Files.createDirectories(Paths.get(uploadPreviewPath));
        }

        Files.createFile(imagePath);
        Files.createFile(Paths.get(previewImageFilename));

        Thumbnails.of(file.getInputStream())
                .size(COMPRESSED_IMAGE_WIDTH, COMPRESSED_IMAGE_HEIGHT)
                .outputQuality(COMPRESSED_IMAGE_QUALITY)
                .toFile(previewImageFilename);
        Files.write(imagePath, file.getBytes());
        return ImageDataDto.toDto(savedEntity);
    }

    @Transactional
    @SneakyThrows
    @PreAuthorize("@projectSecurityService.canEditProject(#projectId)")
    public void deleteFile(Long projectId, UUID imageId) {
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundException("Project with Id=" + projectId + " not found");
        }
        AnnotationImage image = annotationImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image with Id=" + imageId + " not found"));
        annotationImageRepository.deleteById(imageId);
        String uploadPath = localStorageConfigurationProperties.getUploadDefaultPath()
                .replace("{projectId}", projectId.toString());
        String uploadPreviewPath = localStorageConfigurationProperties.getUploadPreviewPath()
                .replace("{projectId}", projectId.toString());
        String imageFilename = getImagePath(
                uploadPath,
                projectId.toString(),
                image.getId().toString(),
                image.getImageName());
        String previewImageFilename = getImagePath(
                uploadPreviewPath,
                projectId.toString(),
                image.getId().toString(),
                image.getImageName());

        Files.delete(Paths.get(imageFilename));
        Files.delete(Paths.get(previewImageFilename));
    }

    private String getImagePath(String basePath, String projectId, String imageId, String imageName) {
        return basePath.replace("{projectId}", projectId)
                + "/" + imageId + "-" + imageName;
    }
}
