package by.pavel.imageannotationbe.serializer;

import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationType;
import by.pavel.imageannotationbe.model.data.BoundingBox;
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
public class BoundingBoxJsonSerializer implements ExportSerializer {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void serialize(List<Annotation> annotations, ZipOutputStream outputStream) {
        outputStream.putNextEntry(new ZipEntry("bboxes.json"));
        PrintWriter printWriter = new PrintWriter(outputStream);
        Map<String, List<BBoxJsonEntry>> bboxes = annotations.stream()
                .filter(annotation -> AnnotationType.BOUNDING_BOX.equals(annotation.getAnnotationType()))
                .collect(groupingBy(BoundingBoxJsonSerializer::imageNameFunc, Collectors.mapping(this::toJsonEntry, toList())));
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
    private BBoxJsonEntry toJsonEntry(Annotation annotation) {
        Integer[] data = BoundingBox.of2PointArray(objectMapper.readValue(annotation.getValue(), Integer[].class)).asPointWHArray();
        return new BBoxJsonEntry(annotation.getId(), data[0], data[1], data[2], data[3]);
    }
}
