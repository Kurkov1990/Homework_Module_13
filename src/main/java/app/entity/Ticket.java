package app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "ticket")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_planet_id", nullable = false)
    private Planet from;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_planet_id", nullable = false)
    private Planet to;
}
