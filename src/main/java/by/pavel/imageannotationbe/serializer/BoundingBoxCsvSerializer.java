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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static by.pavel.imageannotationbe.model.data.BoundingBox.HEIGHT_IDX;
import static by.pavel.imageannotationbe.model.data.BoundingBox.WIDTH_IDX;
import static by.pavel.imageannotationbe.model.data.BoundingBox.X_START_IDX;
import static by.pavel.imageannotationbe.model.data.BoundingBox.Y_START_IDX;

@Service
@RequiredArgsConstructor
public class BoundingBoxCsvSerializer implements ExportSerializer {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void serialize(List<Annotation> annotations, ZipOutputStream outputStream) {
        outputStream.putNextEntry(new ZipEntry("bboxes.csv"));
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("name;tag;x;y;width;height\n");
        annotations.stream().filter(annotation -> AnnotationType.BOUNDING_BOX.equals(annotation.getAnnotationType()))
                .forEach(annotation -> writeBbox(printWriter, annotation));
        printWriter.flush();
        outputStream.closeEntry();
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.CSV;
    }

    @SneakyThrows
    private void writeBbox(PrintWriter printWriter, Annotation annotation) {
        Integer[] pointsArray = objectMapper.readValue(annotation.getValue(), Integer[].class);
        Integer[] data = BoundingBox.of2PointArray(pointsArray).asPointWHArray();
        String formatedBboxData = String.format(
                "%s;%d;%d;%d;%d;%d\n", annotation.getAnnotationImage().getImageName(),
                annotation.getAnnotationTag().getId(),
                data[X_START_IDX],
                data[Y_START_IDX],
                data[WIDTH_IDX],
                data[HEIGHT_IDX]);
        printWriter.write(formatedBboxData);
    }

}
