package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name")
    private String name;

    @Column(name = "project_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectRole> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "annotation_type_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "annotation_type_id"))
    private Set<AnnotationType> allowedAnnotationTypes;
}
