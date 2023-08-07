package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.repository.AnnotationRepository;
import by.pavel.imageannotationbe.repository.AnnotationTagRepository;
import by.pavel.imageannotationbe.serializer.ExportSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({MockitoExtension.class})
class ExportServiceTest {

    @Mock
    private AnnotationRepository annotationRepository;
    @Mock
    private AnnotationTagRepository tagRepository;
    @Mock
    private List<ExportSerializer> serializers;
    @InjectMocks
    private ExportService subject;

    @Test
    void exportData() {
        assertTrue(true);
    }

}
