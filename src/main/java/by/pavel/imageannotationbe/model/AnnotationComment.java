package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "annotation_comment")
public class AnnotationComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annotation_id")
    private Annotation annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "text")
    private String text;

}
