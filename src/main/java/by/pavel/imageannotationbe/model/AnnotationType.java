package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "annotation_type")
public class AnnotationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation_name")
    private String name;

    @Column(name = "description")
    private String description;
}
