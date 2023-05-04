package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.data.BoundingBox;
import jakarta.annotation.Nullable;

public record BoundingBoxAnnotationDto(@Nullable Long annotationId, Long annotationTagId, BoundingBox boundingBox) {
}
