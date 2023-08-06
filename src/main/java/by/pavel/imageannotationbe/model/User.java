package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "annotation_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_surname")
    private String surname;

    @Column(name = "user_email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @OrderBy("endDate desc")
    private List<License> licenses;
}
