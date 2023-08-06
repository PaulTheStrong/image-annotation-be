package by.pavel.imageannotationbe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
