package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "annotation_type", columnDefinition = "ANNOTATION_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private AnnotationType allowedAnnotationType;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AnnotationTag> tags;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ProjectInvitation> invitations;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ProjectRole> roles;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AnnotationImage> images;
}
