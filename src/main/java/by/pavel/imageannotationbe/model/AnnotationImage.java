package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
}
