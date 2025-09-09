package app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "planet")
@Data
public class Planet {
    @Id
    @Pattern(regexp = "^[A-Z0-9]+$")
    @Column(length = 50, nullable = false)
    private String id;

    @Size(min = 1, max = 500)
    @Column(length = 500, nullable = false)
    private String name;

    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Ticket> departures = new HashSet<>();

    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Ticket> arrivals = new HashSet<>();
}
