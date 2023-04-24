package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "storage_type_id")
    private StorageType storageType;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;
}
