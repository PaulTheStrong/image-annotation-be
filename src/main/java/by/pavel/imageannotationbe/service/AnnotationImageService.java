package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.config.LocalStorageConfigurationProperties;
import by.pavel.imageannotationbe.dto.ImageDataDto;
import by.pavel.imageannotationbe.exception.NotFoundException;
import by.pavel.imageannotationbe.model.AnnotationImage;
import by.pavel.imageannotationbe.model.Project;
import by.pavel.imageannotationbe.model.StorageType;
import by.pavel.imageannotationbe.repository.AnnotationImageRepository;
import by.pavel.imageannotationbe.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnotationImageService {

    private final AnnotationImageRepository annotationImageRepository;
    private final ProjectRepository projectRepository;
    private final LocalStorageConfigurationProperties localStorageConfigurationProperties;

    public List<ImageDataDto> getAllByProjectId(Long projectId) {
        return annotationImageRepository.getAllByProjectId(projectId)
                .stream()
                .map(ImageDataDto::toDto)
                .toList();
    }

    public ImageDataDto getByProjectIdAndImageId(Long projectId, Long imageId) {
        return annotationImageRepository.getByProjectIdAndId(projectId, imageId)
                .map(ImageDataDto::toDto)
                .orElseThrow(() -> new NotFoundException("Annotation Image not found with " + imageId));
    }

    public byte[] downloadAnnotationImagePreview(Long projectId, Long imageId) {
        return annotationImageRepository.getByProjectIdAndId(projectId, imageId)
                .map(annotationImage -> getImagePath(localStorageConfigurationProperties.getUploadPreviewPath(), annotationImage))
                .map(AnnotationImageService::getFileBytes)
                .orElseGet(() -> new byte[0]);
    }

    public byte[] downloadAnnotationImage(Long projectId, Long imageId) {
        return annotationImageRepository.getByProjectIdAndId(projectId, imageId)
                .map(annotationImage -> getImagePath(localStorageConfigurationProperties.getUploadDefaultPath(), annotationImage))
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

    @Transactional
    @SneakyThrows
    public ImageDataDto uploadFile(Long projectId, MultipartFile file) {
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundException("Project with Id=" + projectId + " not found");
        }
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        AnnotationImage annotationImage = new AnnotationImage(null,
                Project.builder().id(projectId).build(),
                StorageType.LOCAL_STORAGE,
                file.getOriginalFilename(),
                imageWidth,
                imageHeight,
                Collections.emptyList()
        );
        AnnotationImage savedEntity = annotationImageRepository.save(annotationImage);
        String uploadPath = localStorageConfigurationProperties.getUploadDefaultPath().replace("{projectId}", projectId.toString());
        String uploadPreviewPath = localStorageConfigurationProperties.getUploadPreviewPath().replace("{projectId}", projectId.toString());
        String imageFilename = getImagePath(uploadPath, savedEntity);
        String previewImageFilename = getImagePath(localStorageConfigurationProperties.getUploadPreviewPath(), savedEntity);

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
                .size(320, 240)
                .outputQuality(1)
                .toFile(previewImageFilename);
        Files.write(imagePath, file.getBytes());
        return ImageDataDto.toDto(savedEntity);
    }

    private String getImagePath(String basePath, AnnotationImage savedEntity) {
        return basePath.replace("{projectId}", savedEntity.getProject().getId().toString())
                + "/" + savedEntity.getId() + "-" + savedEntity.getImageName();
    }
}
