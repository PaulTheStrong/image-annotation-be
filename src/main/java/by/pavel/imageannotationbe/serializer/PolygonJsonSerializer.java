package by.pavel.imageannotationbe.serializer;

import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PolygonJsonSerializer implements ExportSerializer {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void serialize(List<Annotation> annotations, ZipOutputStream outputStream) {
        outputStream.putNextEntry(new ZipEntry("polygons.json"));
        PrintWriter printWriter = new PrintWriter(outputStream);
        Map<String, List<PolygonJsonEntry>> bboxes = annotations.stream()
                .filter(annotation -> AnnotationType.POLYGON.equals(annotation.getAnnotationType()))
                .collect(groupingBy(
                        PolygonJsonSerializer::imageNameFunc,
                        Collectors.mapping(this::toJsonEntry, toList()))
                );
        printWriter.print(objectMapper.writeValueAsString(bboxes));
        printWriter.flush();
        outputStream.closeEntry();
    }

    private static String imageNameFunc(Annotation annotation) {
        return annotation.getAnnotationImage().getImageName();
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.JSON;
    }

    @SneakyThrows
    private PolygonJsonEntry toJsonEntry(Annotation annotation) {
        int[][] points = objectMapper.readValue(annotation.getValue(), new TypeReference<>() { });
        return new PolygonJsonEntry(annotation.getId(), points);
    }
}
