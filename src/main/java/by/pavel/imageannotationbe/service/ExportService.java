package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.model.Annotation;
import by.pavel.imageannotationbe.model.AnnotationTag;
import by.pavel.imageannotationbe.repository.AnnotationRepository;
import by.pavel.imageannotationbe.repository.AnnotationTagRepository;
import by.pavel.imageannotationbe.serializer.ExportFormat;
import by.pavel.imageannotationbe.serializer.ExportSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final AnnotationRepository annotationRepository;
    private final AnnotationTagRepository tagRepository;
    private final List<ExportSerializer> serializers;

    @SneakyThrows
    public void exportData(Set<UUID> imageIds, Long projectId, OutputStream outputStream, ExportFormat exportFormat) {
        List<Annotation> annotations = annotationRepository.findAllByAnnotationImageIdIn(imageIds);
        List<AnnotationTag> tags = tagRepository.findAllByProjectIdOrderById(projectId);

        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        writeTagsFile(tags, zipOutputStream);
        serializers.stream()
            .filter(serializer -> serializer.getFormat().equals(exportFormat))
            .forEach(serializer -> serializer.serialize(annotations, zipOutputStream));
        zipOutputStream.close();
    }

    private static void writeTagsFile(List<AnnotationTag> tags, ZipOutputStream zipOutputStream) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry("tags.csv"));
        PrintWriter zipPrintWriter = new PrintWriter(zipOutputStream);
        zipPrintWriter.write("tag_id;tag_name\n");
        for (AnnotationTag tag : tags) {
            zipPrintWriter.write(String.format("%d;%s\n", tag.getId(), tag.getName()));
        }
        zipPrintWriter.flush();
        zipOutputStream.closeEntry();
    }

}
