package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;
import lombok.*;

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
}
