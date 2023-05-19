package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "project_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_role_project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "project_role_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_role_role_id")
    private Role role;
}
