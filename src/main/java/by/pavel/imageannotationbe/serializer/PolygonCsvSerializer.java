package by.pavel.imageannotationbe.serializer;

import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class PolygonCsvSerializer implements ExportSerializer {

    @SneakyThrows
    public void serialize(List<Annotation> annotations, ZipOutputStream outputStream) {
        outputStream.putNextEntry(new ZipEntry("polygons.csv"));
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("name;tag;points\n");
        annotations.stream()
                .filter(annotation -> AnnotationType.POLYGON.equals(annotation.getAnnotationType()))
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
        String outputString = String.format(
                "%s;%d;%s\n",
                annotation.getAnnotationImage().getImageName(),
                annotation.getAnnotationTag().getId(),
                annotation.getValue());
        printWriter.write(outputString);
    }



}
