package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "license")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "license_type_id")
    private LicenseType licenseType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

}
