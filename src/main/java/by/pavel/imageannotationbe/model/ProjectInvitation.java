package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "project_invitation")
public class ProjectInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "invited_id")
    private User invitedUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_invitation",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
