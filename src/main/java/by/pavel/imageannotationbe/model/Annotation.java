package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "annotation")
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "annotation_type_id")
    private AnnotationType annotationType;

    @Column(name = "annotation_path")
    private String annotationPath;

    @ManyToOne
    @JoinColumn(name = "storage_type_id")
    private StorageType storageType;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "annotation_status_id")
    private AnnotationStatus status;

    @OneToMany(mappedBy = "annotation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnnotationComment> comments;
}
