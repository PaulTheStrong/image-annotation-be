package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "license_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LicenseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_name")
    private String licenseName;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "file_count_restriction")
    private Integer fileCountRestriction;

    @Column(name = "file_size_restriction")
    private Long fileSizeRestriction;

}
