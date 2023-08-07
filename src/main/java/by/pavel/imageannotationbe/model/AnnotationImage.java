package by.pavel.imageannotationbe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "annotation_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnotationImage {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "storage_type", columnDefinition = "STORAGE_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private StorageType storageType;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_width")
    private Integer width;

    @Column(name = "image_height")
    private Integer height;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt desc")
    private List<ImageComment> comments;

    @OneToMany(mappedBy = "annotationImage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Annotation> annotations;

    @Column(name = "annotation_status", columnDefinition = "ANNOTATION_STATUS")
    @Enumerated(EnumType.ORDINAL)
    private AnnotationStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "annotated_by")
    private User annotatedBy;
}
