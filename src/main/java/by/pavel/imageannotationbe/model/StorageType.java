package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "storage_type")
public class StorageType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "annotation_type_name")
    private String name;

    @Column(name = "annotation_type_description")
    private String description;

}
