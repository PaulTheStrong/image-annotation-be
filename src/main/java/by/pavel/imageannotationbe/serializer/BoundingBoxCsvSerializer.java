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
        Integer[] data = BoundingBox.of2PointArray(objectMapper.readValue(annotation.getValue(), Integer[].class)).asPointWHArray();
        printWriter.write(String.format("%s;%d;%d;%d;%d;%d\n", annotation.getAnnotationImage().getImageName(), annotation.getAnnotationTag().getId(), data[0], data[1], data[2], data[3]));
    }

}
