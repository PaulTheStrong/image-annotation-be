package by.pavel.imageannotationbe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "annotation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "image_id")
    private AnnotationImage annotationImage;

    @Column(name = "annotation_type", columnDefinition = "ANNOTATION_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private AnnotationType annotationType;

    @Column(name = "annotation_path")
    private String annotationPath;

    @Column(name = "storage_type", columnDefinition = "STORAGE_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private StorageType storageType;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "annotation_tag_id")
    private AnnotationTag annotationTag;

}
