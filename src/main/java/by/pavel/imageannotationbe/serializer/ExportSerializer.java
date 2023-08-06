package by.pavel.imageannotationbe.serializer;

import by.pavel.imageannotationbe.model.Annotation;

import java.util.List;
import java.util.zip.ZipOutputStream;

public interface ExportSerializer {

    void serialize(List<Annotation> annotations, ZipOutputStream outputStream);

    ExportFormat getFormat();
}
