package by.pavel.imageannotationbe.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "license_id")
    private License license;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime paymentDateTime;

    private PaymentStatus status;

    private BigDecimal price;

    public enum PaymentStatus {
        PROCESSED, SUCCESS, FAIL
    }
}
