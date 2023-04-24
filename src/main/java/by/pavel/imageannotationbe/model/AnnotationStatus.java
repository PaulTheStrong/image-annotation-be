package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "annotation_status")
public class AnnotationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation_status_description")
    private String description;

    @Column(name = "annotation_status_name")
    private String name;
}
