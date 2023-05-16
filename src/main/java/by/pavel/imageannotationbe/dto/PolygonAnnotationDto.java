package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.data.Point2D;
import jakarta.annotation.Nullable;

import java.util.List;

public record PolygonAnnotationDto(@Nullable Long annotationId, Long annotationTagId, List<Point2D> polygon) {
}
